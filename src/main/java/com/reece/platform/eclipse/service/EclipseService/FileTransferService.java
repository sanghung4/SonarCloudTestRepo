package com.reece.platform.eclipse.service.EclipseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reece.platform.eclipse.exceptions.EclipseLoadCountsException;
import com.reece.platform.eclipse.model.DTO.EclipseLoadCountDto;
import com.reece.platform.eclipse.model.DTO.JobFormDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class FileTransferService {
    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.remoteFilePath}")
    private String remoteFilePath;

    @Value("${sftp.sessionTimeout}")
    private Integer sessionTimeout;

    @Value("${sftp.channelTimeout}")
    private Integer channelTimeout;

    private final JSch jSch;

    private final String privateKeyPath;

    @Autowired
    public FileTransferService(JSch jSch, String privateKeyPath) {
        this.jSch = jSch;
        this.privateKeyPath = privateKeyPath;
    }

    /**
     * Upload the job form data to Eclipse server
     *
     * @param jobFormDTO job form data to upload
     * @return success
     */
    public String uploadJobForm(JobFormDTO jobFormDTO) throws JSchException, IOException, SftpException {
        ChannelSftp channelSftp = null;
        File file = null;
        File taxFile = null;
        try {
            channelSftp = createChannelSftp();
            file = File.createTempFile("JobForm_", ".json");

            if (jobFormDTO.getFile() != null) {
                taxFile = File.createTempFile("TaxDocument_", String.format(".%s", MimeTypeUtils.parseMimeType(jobFormDTO.getFileMimeType()).getSubtype()));
                byte[] data = Base64.decodeBase64(jobFormDTO.getFile());
                try (OutputStream stream = new FileOutputStream(taxFile.getAbsolutePath())) {
                    stream.write(data);
                }
                channelSftp.put(taxFile.getAbsolutePath(), remoteFilePath);
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = generateJobFormMap(jobFormDTO, taxFile == null ? null : taxFile.getName());
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, map);
            channelSftp.put(file.getAbsolutePath(), remoteFilePath);
            return "Job form uploaded successfully.";
        } finally {
            disconnectChannelSftp(channelSftp);
            if (file != null) {
                file.delete();
            }
            if (taxFile != null) {
                taxFile.delete();
            }
        }
    }

    public List<EclipseLoadCountDto> downloadLatestEclipseCountsFile() throws EclipseLoadCountsException {

        ChannelSftp channelSftp = null;

        List<EclipseLoadCountDto> productsForCount = new ArrayList<>();

        try {
            channelSftp = createChannelSftp();

            Vector fileList = channelSftp.ls("/outgoing");
            //Loop starts with i=2 because [0] = "." and [1] = ".." so position 2 would be the first file in the directory
            for (int i = 2; i < fileList.size(); i++) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) fileList.get(i);
                if(entry.getFilename().startsWith("PhysInvDump")){
                    try (InputStream stream = channelSftp.get("/outgoing/" + entry.getFilename())) {
                        addEclipseProductsToCount(productsForCount, stream);
                        channelSftp.rm("/outgoing/" + entry.getFilename());
                    } catch (IOException ce) {
                        //If you have a problem opening a file, just move onto the next one
                        log.error("IOException when loading file: " + entry.getFilename(), ce);
                    }
                }

            }
        } catch (JSchException | SftpException e) {
            log.error("Exception found when loading file", e);
            throw new EclipseLoadCountsException(e);
        }

        return productsForCount;


    }

    private void addEclipseProductsToCount(List<EclipseLoadCountDto> counts, InputStream stream) {
        counts.addAll((List<EclipseLoadCountDto>) new CsvToBeanBuilder(new InputStreamReader(stream))
                .withType(EclipseLoadCountDto.class)
                .build()
                .parse());
    }

    private Map<String, String> generateJobFormMap(JobFormDTO jobFormDTO, String taxFileName) {
        Map<String, String> map = new HashMap<>();
        if (jobFormDTO.getJob() != null) {
            map.put("UserName", jobFormDTO.getJob().getUserName());
            map.put("CustomerName", jobFormDTO.getJob().getCustomerName());
            map.put("CustomerNumber", jobFormDTO.getJob().getCustomerNumber());
            map.put("CCEmail", jobFormDTO.getJob().getEmail());
            map.put("CCPhone", jobFormDTO.getJob().getPhoneNumber());
        }

        if (jobFormDTO.getProject() != null) {
            map.put("PIJobName", jobFormDTO.getProject().getJobName());
            map.put("PILotNumberTrack", jobFormDTO.getProject().getLotNoAndTrack());
            map.put("PIAddress", jobFormDTO.getProject().getStreetLineOne());
            map.put("PICity", jobFormDTO.getProject().getCity());
            map.put("PIState", jobFormDTO.getProject().getState());
            map.put("PIZip", jobFormDTO.getProject().getPostalCode());
            map.put("PIEstimatedProjectAmount", String.valueOf(jobFormDTO.getProject().getEstimatedProjectAmount()));
            map.put("IsTaxable", String.valueOf(jobFormDTO.getProject().getTaxable()));
        }

        if (jobFormDTO.getGeneralContractor() != null) {
            map.put("GCIGeneralContractor", jobFormDTO.getGeneralContractor().getGeneralContractor());
            map.put("GCIAddress", jobFormDTO.getGeneralContractor().getStreetLineOne());
            map.put("GCICity", jobFormDTO.getGeneralContractor().getCity());
            map.put("GCIState", jobFormDTO.getGeneralContractor().getState());
            map.put("GCIZip", jobFormDTO.getGeneralContractor().getPostalCode());
            map.put("GCIPhone", jobFormDTO.getGeneralContractor().getPhoneNumber());
        }

        if (jobFormDTO.getBonding() != null) {
            map.put("BISuretyName", jobFormDTO.getBonding().getSuretyName());
            map.put("BIAddress", jobFormDTO.getBonding().getStreetLineOne());
            map.put("BICity", jobFormDTO.getBonding().getCity());
            map.put("BIState", jobFormDTO.getBonding().getState());
            map.put("BIZip", jobFormDTO.getBonding().getPostalCode());
            map.put("BIPhone", jobFormDTO.getBonding().getPhoneNumber());
            map.put("BIBondNumber", jobFormDTO.getBonding().getBondNumber());
        }

        if (jobFormDTO.getOwner() != null) {
            map.put("OIOwnerName", jobFormDTO.getOwner().getOwnerName());
            map.put("OIAddress", jobFormDTO.getOwner().getStreetLineOne());
            map.put("OICity", jobFormDTO.getOwner().getCity());
            map.put("OIState", jobFormDTO.getOwner().getState());
            map.put("OIZip", jobFormDTO.getOwner().getPostalCode());
            map.put("OIPhone", jobFormDTO.getOwner().getPhoneNumber());
        }

        if (jobFormDTO.getLender() != null) {
            map.put("LILenderName", jobFormDTO.getLender().getLenderName());
            map.put("LIAddress", jobFormDTO.getLender().getStreetLineOne());
            map.put("LICity", jobFormDTO.getLender().getCity());
            map.put("LIState", jobFormDTO.getLender().getState());
            map.put("LIZip", jobFormDTO.getLender().getPostalCode());
            map.put("LIPhone", jobFormDTO.getLender().getPhoneNumber());
            map.put("LILoanNumber", jobFormDTO.getLender().getLoanNumber());
        }

        if (taxFileName != null) {
            map.put("TaxCertificationFileName", taxFileName);
        }

        return map;
    }

    private ChannelSftp createChannelSftp() throws JSchException {
        jSch.addIdentity(privateKeyPath);
        Session session = jSch.getSession(username, host, port);
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);
        session.connect(sessionTimeout);
        Channel channel = session.openChannel("sftp");
        channel.connect(channelTimeout);
        return (ChannelSftp) channel;
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) throws JSchException {
        if (channelSftp == null)
            return;

        if (channelSftp.isConnected())
            channelSftp.disconnect();

        if (channelSftp.getSession() != null)
            channelSftp.getSession().disconnect();
    }
}
