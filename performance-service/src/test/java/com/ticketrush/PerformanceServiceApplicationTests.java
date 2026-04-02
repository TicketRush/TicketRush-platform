package com.ticketrush;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.s3.S3Client;

@ActiveProfiles("test")
@SpringBootTest(classes = PerformanceServiceApplication.class)
@EnableAutoConfiguration(
    exclude = {
      io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration.class,
      io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration.class
    })
class PerformanceServiceApplicationTests {

  @MockitoBean private S3Client s3Client;

  @Test
  void contextLoads() {}
}
