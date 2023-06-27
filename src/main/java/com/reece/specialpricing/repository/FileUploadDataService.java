package com.reece.specialpricing.repository;

public interface FileUploadDataService {
    String uploadFile(byte[] writableBytes, String fileName) throws Exception;
}
