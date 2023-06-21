package com.reece.platform.products.model.DTO;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class InvoiceDTO {

    private String invoiceNumber;
    private String status;
    private String customerPo;
    private String invoiceDate;
    private Double originalAmt;
    private Double openBalance;
    private String age;
    private String invoiceUrl;
    private String jobNumber;
    private String jobName;
    private String contractNumber;

    @SneakyThrows
    public void setInvoiceUrl(String baseUrl, String encryptionKey, String clientGuid, String erpAccountId) {
        String toEncrypt = String.format(
            "custnbr=%s&method=getMyBills&acctnumber=%s&invoice0=%s",
            clientGuid,
            erpAccountId,
            this.invoiceNumber
        );

        String encryptedParam = encrypt(toEncrypt, encryptionKey);
        String url = String.format("%s?custnbr=%s&p=%s", baseUrl, clientGuid, encryptedParam);

        this.invoiceUrl = url;
    }

    public static String encrypt(String toEncrypt, String key)
        throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] keyArray;
        byte[] toEncryptArray = toEncrypt.getBytes("UTF8");

        keyArray = key.getBytes("UTF8");

        SecretKey secretKey = new SecretKeySpec(keyArray, "DESede");
        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] resultArray = cipher.doFinal(toEncryptArray);

        return Base64.getEncoder().encodeToString(resultArray);
    }
}
