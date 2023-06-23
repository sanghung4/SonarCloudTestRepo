package com.reece.punchoutcustomersync.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.reece.punchoutcustomerbff.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class AmazonS3Service {

    @Value("${aws.s3.filePath}")
    private String s3PutFilePath;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadToS3(InputStream csv, String fileName) throws IOException {
        LocalDate now = LocalDate.now();
        String filePath = String.format("%s/%d/%d/%d/%s", s3PutFilePath, now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), fileName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(csv.available());
        objectMetadata.setContentType("text/csv");
        objectMetadata.setContentDisposition(String.format("attachment; filename=%s", fileName));
        s3Client.putObject(s3Bucket, filePath, csv, objectMetadata);
        return filePath;
    }
}
