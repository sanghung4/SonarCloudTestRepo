package com.reece.platform.notifications.notificationservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.reece.platform.notifications.model.DTO.*;
import com.reece.platform.notifications.model.WebStatusesEnum;
import com.reece.platform.notifications.service.S3Service;
import com.reece.platform.notifications.service.SalesforceMarketingCloudService;
import com.reece.platform.notifications.service.SimpleEmailService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

public class SimpleEmailServiceTest {

    private AmazonSimpleEmailService emailServiceClient;
    private SimpleEmailService emailService;
    private ContactFormDTO contactFormDTO;
    static final String CONTACT_FORM_TEMPLATE_KEY = "ContactFormTemplate.mustache";

    @Spy
    private SimpleEmailService mockEmailService;

    private SalesforceMarketingCloudService salesforceMarketingCloudService;
    private S3Service s3Service;

    @Spy
    private S3Service mockS3Service;

    private S3Object templateObject;

    @Spy
    private S3Object mockTemplateObject;

    private AmazonS3 client;
    private S3Service s3Service1;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        emailServiceClient = mock(AmazonSimpleEmailService.class);
        s3Service = mock(S3Service.class);
        emailService = new SimpleEmailService(emailServiceClient, s3Service);
        salesforceMarketingCloudService = mock(SalesforceMarketingCloudService.class);
        templateObject = mock(S3Object.class);

        contactFormDTO = new ContactFormDTO();
        contactFormDTO.setFirstName("firstName");
        contactFormDTO.setLastName("lastName");
        contactFormDTO.setPhoneNumber("phoneNumber");
        contactFormDTO.setEmail("email");
        contactFormDTO.setZip("zip");
        contactFormDTO.setTopic("topic");
        contactFormDTO.setMessage("message");

        String[] supportEmailsTest = new String[]{"test@morsco.com"};
        ReflectionTestUtils.setField(emailService, "supportEmails", supportEmailsTest);

        String s3TemplateBucketTest = "bucket";
        ReflectionTestUtils.setField(emailService, "s3TemplateBucket", s3TemplateBucketTest);

        client = mock(AmazonS3.class);
        s3Service1 = new S3Service(client);
    }

    @Test
    void buildDestination_toAddresses() {
        List<String> toAddresses = Arrays.asList("test@test.com", "test2@test.com");
        Destination result = emailService.buildDestination(toAddresses);
        assertEquals(result.getToAddresses().size(), 2);
        assertEquals(result.getToAddresses(), toAddresses);
    }

    @Test
    void buildDestination_allAddresses() {
        List<String> toAddresses = Arrays.asList("test@test.com", "test2@test.com");
        List<String> ccAddresses = Arrays.asList("ccAddress@test.com", "ccAddress2@test.com");
        List<String> bccAddresses = Arrays.asList(
                "bccAddress@test.com",
                "bccAddress2@test.com",
                "bccAddress3@test.com"
        );
        Destination result = emailService.buildDestination(toAddresses, ccAddresses, bccAddresses);
        assertEquals(result.getToAddresses().size(), 2);
        assertEquals(result.getToAddresses(), toAddresses);
        assertEquals(result.getCcAddresses().size(), 2);
        assertEquals(result.getCcAddresses(), ccAddresses);
        assertEquals(result.getBccAddresses().size(), 3);
        assertEquals(result.getBccAddresses(), bccAddresses);
    }

    @Test
    void sendEmail_success() {
        when(emailServiceClient.sendEmail(any(SendEmailRequest.class))).thenReturn(any(SendEmailResult.class));
        Destination destination = emailService.buildDestination(Collections.singletonList("hello@world.com"));
        Message message = emailService.buildMessage("Subject", "HtmlBody", "TextBody");
        ResponseEntity<String> response = emailService.sendEmail(destination, message, "replyToEmail");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Email sent successfully!");
    }

    @Test
    void sendEmail_fail() {
        when(emailServiceClient.sendEmail(any(SendEmailRequest.class)))
                .thenThrow(new MessageRejectedException("exception"));
        Destination destination = emailService.buildDestination(Collections.singletonList("hello@world.com"));
        Message message = emailService.buildMessage("Subject", "HtmlBody", "TextBody");
        assertThrows(MessageRejectedException.class, () -> emailService.sendEmail(destination, message, null));
    }

    @Test
    void sendContactForm() throws IOException {
        String subject = "Contact Us Submission";
        List<String> toAddresses = Arrays.asList("test@test.com", "test2@test.com");
        mockEmailService = Mockito.spy(emailService);
        doReturn(new ResponseEntity<>("Email sent successfully!", HttpStatus.OK))
                .when(mockEmailService)
                .sendEmail(any(Destination.class), any(), any());
        doReturn(new Destination().withToAddresses(toAddresses)).when(mockEmailService).buildDestination(any());
        doReturn("Email sent successfully!").when(mockEmailService).buildHtmlBody(any(), any());
        ResponseEntity<String> response = mockEmailService.sendContactForm(contactFormDTO);
        assertEquals("Email sent successfully!", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendSubmitOrderEmail() throws IOException {
        mockEmailService = Mockito.spy(emailService);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductDescription("description");
        productDTO.setUnitPrice("50.25");
        productDTO.setQuantity(9);
        productDTO.setProductTotal("150");

        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        submitOrderDTO.setBranchDTO(new BranchAddressDTO());
        submitOrderDTO.setAddress(new BaseAddressDTO());
        submitOrderDTO.setProductDTOs(new ArrayList<>());
        submitOrderDTO.setTax("1.20");
        submitOrderDTO.setSubTotal("20.10");
        submitOrderDTO.setTotal("32.10");
        submitOrderDTO.setEmails(Arrays.asList("email"));
        submitOrderDTO.setWebStatus(WebStatusesEnum.READY_FOR_PICKUP.name());
        submitOrderDTO.setProductDTOs(List.of(productDTO));

        submitOrderDTO.setTotal(salesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTotal()));
        submitOrderDTO.setSubTotal(salesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getSubTotal()));
        submitOrderDTO.setTax(salesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTax()));

        for (ProductDTO productDTOOb : submitOrderDTO.getProductDTOs()) {
            productDTOOb.setUnitPrice(salesforceMarketingCloudService.checkPriceFormat(productDTOOb.getUnitPrice()));
        }

        Destination destination = mockEmailService.buildDestination(
                Collections.singletonList(submitOrderDTO.getEmails().get(0))
        );
        String html = "Email sent successfully!";
        doReturn("Email sent successfully!").when(mockEmailService).buildHtmlBody(any(), any());
        Message message = mockEmailService.buildMessage("Order Received", html);

        ResponseEntity<String> response = mockEmailService.sendEmail(destination, message, null);
        assertEquals("Email sent successfully!", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<String> response2 = mockEmailService.sendSubmitOrderEmail(submitOrderDTO);
        assertEquals("Email sent successfully!", response2.getBody());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    void buildHtmlBody() throws IOException {
        mockEmailService = Mockito.spy(emailService);
        mockS3Service = Mockito.spy(s3Service);
        mockTemplateObject = Mockito.spy(templateObject);
        String key = CONTACT_FORM_TEMPLATE_KEY;
        Object templateData = contactFormDTO;
        String s3TemplateBucket = "bucket";

        S3Object templateObject = new S3Object();
        doReturn(templateObject).when(mockS3Service).getTemplate(any(), any());

        InputStream inputStream = new ByteArrayInputStream("html".getBytes(Charset.forName("UTF-8")));
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(inputStream, null);
        doReturn(s3ObjectInputStream).when(mockTemplateObject).getObjectContent();
        assertThrows(Exception.class, () -> mockEmailService.buildHtmlBody(key, templateData));
    }

    @Test
    void buildHtmlBody_success() throws Exception {
        mockEmailService = Mockito.spy(emailService);
        S3Object templateObject = mock(S3Object.class);
        mockTemplateObject = Mockito.spy(templateObject);
        doReturn(templateObject).when(s3Service).getTemplate(any(), any());
        String key = CONTACT_FORM_TEMPLATE_KEY;
        Object templateData = contactFormDTO;
        InputStream inputStream = new ByteArrayInputStream("html".getBytes(Charset.forName("UTF-8")));
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(inputStream, null);
        doReturn(s3ObjectInputStream).when(templateObject).getObjectContent();
        String result = mockEmailService.buildHtmlBody(key, templateData);
        assertEquals("html", result);
    }

    @Test
    void getTemplate() {
        String bucket = "443";
        String key = "key";
        S3Object s3Object = mock(S3Object.class);

        when(client.getObject(bucket, key)).thenReturn(s3Object);
        assertDoesNotThrow(() -> s3Service1.getTemplate(bucket, key));
    }
}
