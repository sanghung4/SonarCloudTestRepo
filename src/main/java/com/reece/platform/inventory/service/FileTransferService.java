package com.reece.platform.inventory.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reece.platform.inventory.dto.EclipseLoadCountDto;
import com.reece.platform.inventory.exception.EclipseLoadCountsException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileTransferService {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.sessionTimeout}")
    private Integer sessionTimeout;

    @Value("${sftp.channelTimeout}")
    private Integer channelTimeout;

    private final LoadCountService loadCountService;
    private final JSch jSch;
    private final String privateKeyPath;

    private static final String SFTP_FOLDER_OUTGOING = "/outgoing";
    private static final String SFTP_FOLDER_PROCESSED = "/outgoing/processed";
    private static final String SFTP_FOLDER_ERROR = "/outgoing/error";
    private static final String SFTP_PHYS_INV_PREFIX = "PhysInvDump";

    public List<EclipseLoadCountDto> downloadLatestEclipseCountsFile() throws EclipseLoadCountsException {
        List<EclipseLoadCountDto> productsForCount = new ArrayList<>();

        // Pairs a file name with a list of CountId values
        HashMap<String, String> fileBatchMap = new HashMap<>();

        try {
            ChannelSftp channelSftp = createChannelSftp();

            verifyFolderSFTP(SFTP_FOLDER_PROCESSED, channelSftp);

            final List<LsEntry> matchingFileList = getSFTPFilesList(channelSftp);

            if (!matchingFileList.isEmpty()) {
                for (LsEntry entry : matchingFileList) {
                    try (InputStream stream = channelSftp.get(SFTP_FOLDER_OUTGOING + "/" + entry.getFilename())) {
                        addEclipseProductsToCount(productsForCount, stream);
                        // Pairs filename with batchNumber
                        fileBatchMap.put(
                            entry.getFilename(),
                            productsForCount
                                .stream()
                                .filter((EclipseLoadCountDto product) ->
                                    entry.getFilename().contains(product.getBatchNumber())
                                )
                                .map(EclipseLoadCountDto::getBatchNumber)
                                .findFirst()
                                .orElse(null)
                        );
                    } catch (Exception ce) {
                        /* If file has an error, will be moved to /outgoing/error folder inside SFTP, so when the batch runs again
                		it does not try to process it again */
                        verifyFolderSFTP(SFTP_FOLDER_ERROR, channelSftp);
                        channelSftp.rename(
                            SFTP_FOLDER_OUTGOING + "/" + entry.getFilename(),
                            SFTP_FOLDER_ERROR + "/" + entry.getFilename()
                        );
                        // If an exception occurs opening a file, just move onto the next one
                        log.error(
                            "Exception when loading file: {}, will be moved to error folder: {}",
                            entry.getFilename(),
                            ce
                        );
                    }
                }
            }

            loadEclipseBatch(productsForCount, fileBatchMap, channelSftp);

            disconnectChannelSftp(channelSftp);
        } catch (JSchException | SftpException e) {
            log.error("Exception found when loading file {}", e);
            throw new EclipseLoadCountsException(e);
        } catch (Exception e) {
            log.error("There was a general exception loading counts {}", e);
        }

        return productsForCount;
    }

    /**
     * Calls loadCountService to load a single count from a provided list. Also
     * manages the mv command from channelSftp when file loads successfully.
     *
     * @param eclipseCounts list of EclipseLoadCountDto
     * @param fileBatchMap map pairing getBatchNumber and file name
     * @param channelSftp channelSftp instance of ChannelSftp
     */
    private void loadEclipseBatch(
        List<EclipseLoadCountDto> eclipseCounts,
        HashMap<String, String> fileBatchMap,
        ChannelSftp channelSftp
    ) {
        HashMap<String, List<EclipseLoadCountDto>> productsMap = new HashMap<>();

        for (EclipseLoadCountDto product : eclipseCounts) {
            if (!productsMap.containsKey(product.getBatchNumber())) {
                productsMap.put(product.getBatchNumber(), new ArrayList<>());
            }
            productsMap.get(product.getBatchNumber()).add(product);
        }

        productsMap
            .keySet()
            .forEach(countId -> {
                try {
                    loadCountService.loadCountFromProductList(countId, productsMap.get(countId));
                    var filename = fileBatchMap
                        .entrySet()
                        .stream()
                        .filter(fileBatch -> fileBatch.getKey().startsWith(SFTP_PHYS_INV_PREFIX + "_" + countId))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet())
                        .toString()
                        .replaceAll("[\\[\\]]", "");
                    channelSftp.rename(SFTP_FOLDER_OUTGOING + "/" + filename, SFTP_FOLDER_PROCESSED + "/" + filename);
                } catch (Exception e) {
                    log.error("Unable to load count for id {} : {}", countId, e);
                }
            });
    }

    /**
     *
     * Returns a com.jcraft.jsch.ChannelSftp.LsEntry List depending on the route and prefix provided.
     *
     * @param channelSftp instance of ChannelSftp
     * @return List of type com.jcraft.jsch.ChannelSftp.LsEntry
     * @throws SftpException com.jcraft.jsch exception
     */
    private List<LsEntry> getSFTPFilesList(ChannelSftp channelSftp) throws SftpException {
        return (List<LsEntry>) (channelSftp.ls(SFTP_FOLDER_OUTGOING)).stream()
            .filter(lsEntry -> ((LsEntry) lsEntry).getFilename().startsWith(SFTP_PHYS_INV_PREFIX))
            .collect(Collectors.toList());
    }

    /**
     *
     * Method used to verify if a folder exists in SFTP server. If not,
     * it will create the folder in provided in param folderName route
     *
     * @param folderName that will be validated
     * @param channelSftp instance of ChannelSftp
     * @throws SftpException com.jcraft.jsch exception
     */
    private void verifyFolderSFTP(String folderName, ChannelSftp channelSftp) throws SftpException {
        SftpATTRS sftpAttributes = null;

        try {
            sftpAttributes = channelSftp.stat(folderName);
        } catch (Exception e) {
            log.info("Folder {} not found. Will use mkdir ...", folderName);
        }

        if (sftpAttributes == null) {
            channelSftp.mkdir(folderName);
        }
    }

    private void addEclipseProductsToCount(List<EclipseLoadCountDto> counts, InputStream stream) {
        counts.addAll(
            new CsvToBeanBuilder<EclipseLoadCountDto>(new InputStreamReader(stream))
                .withType(EclipseLoadCountDto.class)
                .build()
                .parse()
        );
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
        if (channelSftp == null) return;

        if (channelSftp.isConnected()) channelSftp.disconnect();

        if (channelSftp.getSession() != null) channelSftp.getSession().disconnect();
    }
}
