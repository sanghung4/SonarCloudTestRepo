package com.reece.specialpricing.repository;

import jcifs.smb1.smb1.NtlmPasswordAuthentication;
import jcifs.smb1.smb1.SmbFile;
import jcifs.smb1.smb1.SmbFileOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service("smbUploadService")
public class SmbDataServiceImpl implements FileUploadDataService {

    @Value("${com.reece.smb.address}")
    private String address;

    @Value("${com.reece.smb.username}")
    private String username;

    @Value("${com.reece.smb.password}")
    private String password;

    @Override
    public String uploadFile(byte[] writableBytes, String fileName) throws Exception {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", username, password);

        String destDir = StringUtils.cleanPath("smb://" + address);
        SmbFileOutputStream fos = null;
        String destFilePath = null;
        try {
            //create directory structure
            SmbFile sfileDir = new SmbFile(destDir, auth);
            if(!sfileDir.exists()) {
                sfileDir.mkdirs();
            }
            destFilePath = StringUtils.cleanPath(destDir + fileName);
            SmbFile sfile = new SmbFile(destFilePath, auth);
            fos = new SmbFileOutputStream(sfile);
            fos.write(writableBytes);
        } catch(IOException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            writableBytes = null;
            if(fos!=null && fos.isOpen()){
                fos.close();
            }
        }
        if (destFilePath!=null){
            return destFilePath.replace("smb:", "");
        }
        return null;
    }
}
