package com.ticketrush.boundedcontext.performance.in.api.v1;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ticketrush.boundedcontext.performance.app.facade.PerformanceFacade;
import com.ticketrush.boundedcontext.performance.domain.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.domain.types.Genre;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(PerformanceAdminController.class)
class PerformanceValidationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PerformanceFacade performanceFacade;

  private final String baseUrl = "/performance";

  @Test
  @WithMockUser
  @DisplayName("공연명이 비어있으면 에러가 발생한다")
  void titleNotBlankValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address("서울시")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID_400"));
  }

  @Test
  @WithMockUser
  @DisplayName("가격이 음수이면 에러가 발생한다")
  void pricePositiveValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("정상 제목")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(-1000L)
            .totalSeats(100)
            .address("서울시")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID_400"));
  }

  @Test
  @WithMockUser
  @DisplayName("장르를 선택하지 않으면 에러가 발생한다")
  void genreNotNullValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("정상 제목")
            .performer("정상 가수")
            .genre(null)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address("서울시")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID_400"));
  }

  @Test
  @WithMockUser
  @DisplayName("공연장 주소가 비어있으면 에러가 발생한다")
  void addressNotBlankValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("정상 제목")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address("")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID_400"));
  }

  @Test
  @WithMockUser
  @DisplayName("여러 필드가 동시에 유효하지 않으면 VALID401 에러가 발생한다")
  void complexValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(-50000L)
            .totalSeats(100)
            .address("")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID401"))
        .andExpect(jsonPath("$.isSuccess").value(false));
  }

  @Test
  @WithMockUser
  @DisplayName("필수 필드가 누락되거나 잘못되면 VALID401 에러가 발생한다")
  void mandatoryFieldsValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("정상 제목")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(null)
            .showTime(null)
            .durationMinutes(0)
            .price(50000L)
            .totalSeats(-1)
            .address("서울시")
            .imageMainUrl("")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID401"))
        .andExpect(jsonPath("$.isSuccess").value(false))
        .andExpect(jsonPath("$.result.showDate").exists())
        .andExpect(jsonPath("$.result.showTime").exists())
        .andExpect(jsonPath("$.result.totalSeats").exists())
        .andExpect(jsonPath("$.result.imageMainUrl").exists());
  }

  @Test
  @WithMockUser
  @DisplayName("필수 문자열 필드에 공백(\" \")만 입력되면 VALID401 에러가 발생한다")
  void whiteSpaceValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("   ")
            .performer("정상 가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(120)
            .price(50000L)
            .totalSeats(100)
            .address(" ")
            .imageMainUrl("https://image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID401"))
        .andExpect(jsonPath("$.isSuccess").value(false))
        .andExpect(jsonPath("$.result.title").exists())
        .andExpect(jsonPath("$.result.address").exists());
  }

  @Test
  @WithMockUser
  @DisplayName("모든 값이 올바르면 201 응답을 반환한다")
  void createPerformanceSuccess() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("정상 공연")
            .performer("아티스트")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .durationMinutes(150)
            .price(50000L)
            .totalSeats(100)
            .address("서울시")
            .imageMainUrl("https://ticketrush.com/image.png")
            .build();

    mockMvc
        .perform(
            post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.isSuccess").value(true));
  }
}
