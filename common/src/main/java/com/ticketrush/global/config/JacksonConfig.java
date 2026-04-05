package com.ticketrush.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Bean
  public JsonMapperBuilderCustomizer jacksonCustomizer() {
    return builder -> {
      // Null 값 제외 설정 (응답에서 제외)
      builder.changeDefaultPropertyInclusion(
          incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL));

      //  Java 8 날짜 포맷 전역 설정
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

      SimpleModule timeModule = new SimpleModule();
      timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
      timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
      builder.addModule(timeModule);
    };
  }
}
