package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.dto.ResultDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Used for refreshing test related data.
 * @author john.valentino
 */
@Slf4j
@Service
public class RefreshService {

  @Autowired
  private EntityManager entityManager;

  @Transactional
  public ResultDto refresh() {
    ResultDto result = new ResultDto();

    String text = null;

    try {
      Resource resource = new ClassPathResource("/test-data.sql");
      text = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      result.setMessage(text);
    } catch (IOException e) {
      log.error("Unknown", e);
    }

    entityManager.createNativeQuery(text).executeUpdate();

    return result;
  }

}
