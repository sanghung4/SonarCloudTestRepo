package com.reece;

import static org.mockito.Mockito.mockStatic;

import com.reece.PunchoutCustomerCatalogApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.SpringApplication;

/**
 * SpringBoot Test Class
 */
@PrepareForTest(PunchoutCustomerCatalogApplication.class)
public class PunchoutCustomerCatalogApplicationTests {


  private MockedStatic<SpringApplication> mockSpring;

  @BeforeEach
  public void before() {
    new PunchoutCustomerCatalogApplication();
    mockSpring = mockStatic(SpringApplication.class);
  }

  @AfterEach
  public void after() {
    mockSpring.close();
  }

  @Test
  public void testMain() {
    // given
    String[] args = new String[] {};

    // when
    PunchoutCustomerCatalogApplication.main(args);

    // then
    SpringApplication.run(PunchoutCustomerCatalogApplication.class, args);
  }
}
