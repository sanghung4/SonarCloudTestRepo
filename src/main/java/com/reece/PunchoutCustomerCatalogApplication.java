package com.reece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of application.
 *
 * @author luis.bolivar
 */
@SpringBootApplication
public class PunchoutCustomerCatalogApplication {

  /**
   * Main method that run that allow run the application.
   *
   * @param args for the application that receive from execution
   */
  public static void main(final String[] args) {
    SpringApplication.run(PunchoutCustomerCatalogApplication.class, args);
  }
}