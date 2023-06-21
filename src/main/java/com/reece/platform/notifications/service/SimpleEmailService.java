package com.reece.platform.notifications.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.reece.platform.notifications.model.DTO.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SimpleEmailService {
    private static AmazonSimpleEmailService client;
    private static final MustacheFactory mf = new DefaultMustacheFactory();

    private String secretAccessKey;
    private String accessKeyId;
    private String region;

    @Value("${fromEmail}")
    private String fromEmail;

    @Value("${templateBucket}")
    private String s3TemplateBucket;

    @Value("${portalUrl}")
    private String portalUrl;

    @Value("${supportEmails:test@morsco.com}")
    private String[] supportEmails;

    private BasicAWSCredentials awsCredentials;
    private AWSStaticCredentialsProvider credentialsProvider;

    @Autowired
    private S3Service s3Service;

    static final String ORDER_SUBMIT_TEMPLATE_KEY = "OrderSubmitTemplate.mustache";
    static final String CONTACT_FORM_TEMPLATE_KEY = "ContactFormTemplate.mustache";

    @Autowired
    public SimpleEmailService(
        @Value("${secretAccessKey}") String secretAccessKey,
        @Value("${accessKeyId}") String accessKeyId,
        @Value("${region}") String region,
        S3Service s3Service
    ) {
        this.s3Service = s3Service;
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.region = region;
        this.awsCredentials =  new BasicAWSCredentials(this.accessKeyId, this.secretAccessKey);
        this.credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        this.client = AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(this.region)
                .build();
    }

    public SimpleEmailService(AmazonSimpleEmailService _client, S3Service _s3Service ) {
        client = _client;
        s3Service = _s3Service;
    }

    public ResponseEntity<String> sendSubmitOrderEmail(@NotNull SubmitOrderDTO submitOrderDTO) throws IOException {
        submitOrderDTO.setTotal(SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTotal()));
        submitOrderDTO.setSubTotal(SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getSubTotal()));
        submitOrderDTO.setTax(SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTax()));

        for (ProductDTO productDTO : submitOrderDTO.getProductDTOs()) {
            productDTO.setUnitPrice(SalesforceMarketingCloudService.checkPriceFormat(productDTO.getUnitPrice()));
        }

        String subject = "Order Received";
        Destination destination = buildDestination(Collections.singletonList(submitOrderDTO.getEmails().get(0)));
        String html = buildHtmlBody(ORDER_SUBMIT_TEMPLATE_KEY, submitOrderDTO);
        Message message = buildMessage(subject, html);
        return sendEmail(destination, message, null);
    }

    public ResponseEntity<String> sendContactForm(@NotNull ContactFormDTO contactFormDTO) throws IOException {
        String subject = "Contact Us Submission";
        Destination destination = buildDestination(Arrays.asList(supportEmails));
        String html = buildHtmlBody(CONTACT_FORM_TEMPLATE_KEY, contactFormDTO);
        Message message = buildMessage(subject, html);
        return sendEmail(destination, message, contactFormDTO.getEmail());
    }

    public String buildHtmlBody(String key, Object templateData) throws IOException {
        S3Object templateObject = s3Service.getTemplate(s3TemplateBucket, key);
        InputStream stream = templateObject.getObjectContent();
        Path path = Files.createDirectories(Paths.get(".tmp"));
        File directory = new File(".tmp");
        String[] fileProps = key.split("\\.");
        File tempFile = File.createTempFile(fileProps[0], fileProps[1], directory);
        Files.copy(stream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        StringWriter writer = new StringWriter();
        Mustache m = mf.compile(tempFile.toPath().toString());
        m.execute(writer, templateData).flush();
        tempFile.delete();
        stream.close();
        Files.delete(path);
        return writer.toString();
    }

    public Destination buildDestination(List<String> toAddresses) {
        return new Destination().withToAddresses(toAddresses);
    }

    public Destination buildDestination(List<String> toAddresses, List<String> ccAddresses, List<String> bccAddresses) {
        Destination destination = new Destination().withToAddresses(toAddresses);
        if (!ccAddresses.isEmpty()) {
            destination.withCcAddresses(ccAddresses);
        }
        if (!bccAddresses.isEmpty()) {
            destination.withBccAddresses(bccAddresses);
        }
        return destination;
    }

    public Content buildContent(String content) {
        return new Content().withCharset("UTF-8").withData(content);
    }

    public Message buildMessage(String subject, String htmlBody, String textBody) {
        Body body = new Body().withHtml(buildContent(htmlBody)).withText(buildContent(textBody));
        return new Message().withBody(body).withSubject(buildContent(subject));
    }
    public Message buildMessage(String subject, String htmlBody) {
        Body body = new Body().withHtml(buildContent(htmlBody));
        return new Message().withBody(body).withSubject(buildContent(subject));
    }

    public ResponseEntity<String> sendEmail(Destination destination, Message message, String replyToEmail) throws
            MessageRejectedException,
            MailFromDomainNotVerifiedException,
            ConfigurationSetDoesNotExistException,
            ConfigurationSetSendingPausedException,
            AccountSendingPausedException
    {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(destination)
                .withMessage(message)
                .withSource(fromEmail);

        if (replyToEmail != null) {
            request.withReplyToAddresses(Collections.singletonList(replyToEmail));
        }

        client.sendEmail(request);
        return new ResponseEntity<>("Email sent successfully!", HttpStatus.OK);
    }
}
