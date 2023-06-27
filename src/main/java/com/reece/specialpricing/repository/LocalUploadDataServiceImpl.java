package com.reece.specialpricing.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service("localUploadService")
public class LocalUploadDataServiceImpl implements FileUploadDataService {
    @Value("${com.reece.pricingUpload.mockFileSave}")
    private boolean mockFileSave;

    public String uploadFile(byte[] writableBytes, String fileName) throws Exception{
        var defaultDirectoryPath = String.format("%s%s%s", FileSystemView.getFileSystemView().getDefaultDirectory().getPath(), File.separator, "changeSuggestions");
        if(mockFileSave){
            return String.format("%s%s%s", defaultDirectoryPath, File.separator, fileName);
        }
        OutputStream outputStream = null;
        try {
            new File(defaultDirectoryPath).mkdir();
            var fullFilePath = String.format("%s%s%s", defaultDirectoryPath, File.separator, fileName);
            outputStream = new FileOutputStream(fullFilePath);
            outputStream.write(writableBytes);
            return fullFilePath;
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        } finally{
            writableBytes = null;
            if(outputStream!=null){
                outputStream.close();
            }
        }
        return null;
    }
}