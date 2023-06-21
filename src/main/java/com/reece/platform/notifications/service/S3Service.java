package com.reece.platform.notifications.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {
    private AmazonS3 client;

    @Value("${fromEmail}")
    private String fromEmail;

    @Value("${templateBucket}")
    private String s3TemplateBucket;

    private BasicAWSCredentials awsCredentials;
    private AWSStaticCredentialsProvider credentialsProvider;

    @Autowired
    public S3Service(
        @Value("${secretAccessKey}") String secretAccessKey,
        @Value("${accessKeyId}") String accessKeyId,
        @Value("${region}") String region
    ) {
        awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }

    public S3Service(AmazonS3 _client) {
        client = _client;
    }

    public S3Object getTemplate(String bucket, String key) {
        return client.getObject(bucket, key);
    }
}
