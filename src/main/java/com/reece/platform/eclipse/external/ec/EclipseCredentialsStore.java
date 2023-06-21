package com.reece.platform.eclipse.external.ec;

import com.reece.platform.eclipse.exceptions.NoEclipseCredentialsException;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class EclipseCredentialsStore {
    private final S3Client s3Client;

    @Value("${eclipse_credentials_store_bucket}")
    private String bucketName;

    public EclipseCredentialsStore() {
        s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();

    }

    public EclipseCredentials getCredentials(String userId, String sessionId) {
        val getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(userId)
                .build();

        try {
            val resp = s3Client.getObjectAsBytes(getRequest);
            val credentialPair = resp.asString(Charset.defaultCharset());
            return EclipseCredentials.fromStorageFormat(credentialPair, sessionId);
        } catch (S3Exception e) {
            // The k8s pod doesn't have the s3:ListObjects action, so the error with be a 403
            // When I test locally, my account created the bucket so I have s3:ListObjects, the error is a 404
            if (403 == e.statusCode() || 404 == e.statusCode()) {
                throw new NoEclipseCredentialsException();
            }

            throw e;
        }
    }

    public void putCredentials(String userId, EclipseCredentials credentials) {
        val putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(userId)
                .build();

        val credentialPair = credentials.toStorageFormat();
        val requestBody = RequestBody.fromByteBuffer(StandardCharsets.UTF_8.encode(credentialPair));

        s3Client.putObject(putRequest, requestBody);
    }
}
