package com.reece.platform.products.utilities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;

public class InvoiceUtils {

    public String getInvoiceUrl(
        String erpAccountId,
        String invoiceNumber,
        String billtrustBaseUrl,
        String billtrustEncryptionKey,
        String billtrustClientGuid
    ) {
        return getInvoiceUrl(
            erpAccountId,
            Arrays.asList(invoiceNumber),
            billtrustBaseUrl,
            billtrustEncryptionKey,
            billtrustClientGuid
        );
    }

    @SneakyThrows
    public String getInvoiceUrl(
        String erpAccountId,
        List<String> invoiceNumbers,
        String billtrustBaseUrl,
        String billtrustEncryptionKey,
        String billtrustClientGuid
    ) {
        String invoicesParam = "";
        Integer invoiceCount = 0;

        for (String invoiceNumber : invoiceNumbers) {
            invoicesParam += String.format("&invoice%s=%s", invoiceCount.toString(), invoiceNumber);
            invoiceCount++;
        }
        String toEncrypt = String.format(
            "custnbr=%s&method=getMyBills&acctnumber=%s%s",
            billtrustClientGuid,
            erpAccountId,
            invoicesParam
        );

        String encryptedParam = encrypt(toEncrypt, billtrustEncryptionKey);

        String url = String.format("%s?custnbr=%s&p=%s", billtrustBaseUrl, billtrustClientGuid, encryptedParam);
        return url;
    }

    private String encrypt(String toEncrypt, String key)
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
