package com.ticketrush.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String TIME_FORMAT = "HH:mm:ss";

  @Bean
  public JsonMapperBuilderCustomizer jacksonCustomizer() {
    return builder -> {
      // 1. Naming Strategy 설정 (camelCase -> snake_case)
      builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

      // 2. Null 값 제외 설정 (응답에서 제외)
      builder.changeDefaultPropertyInclusion(
          incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL));

      // 3. Java 8 날짜 포맷 전역 설정
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

      SimpleModule timeModule = new SimpleModule();
      timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
      timeModule.addDeserializer(
          LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
      timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
      timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
      timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
      timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
      builder.addModule(timeModule);
    };
  }
}
