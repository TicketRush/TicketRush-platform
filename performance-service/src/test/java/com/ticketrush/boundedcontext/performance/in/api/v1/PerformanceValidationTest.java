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

  @Test
  @WithMockUser
  @DisplayName("공연명이 비어있으면 에러가 발생한다")
  void titleNotBlankValidationFail() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("")
            .price(50000L)
            .totalSeats(100)
            .showDate(LocalDate.now())
            .build();

    mockMvc
        .perform(
            post("/api/v1/performance")
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
            .price(-1000L)
            .totalSeats(100)
            .showDate(LocalDate.now())
            .build();

    mockMvc
        .perform(
            post("/api/v1/performance")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALID_400"));
  }

  @Test
  @WithMockUser
  @DisplayName("모든 값이 올바르면 201 응답을 반환한다")
  void createPerformanceSuccess() throws Exception {
    PerformanceCreateRequest request =
        PerformanceCreateRequest.builder()
            .title("콘서트")
            .performer("가수")
            .genre(Genre.CONCERT)
            .showDate(LocalDate.now())
            .showTime(LocalTime.of(19, 0))
            .price(50000L)
            .totalSeats(100)
            .address("서울")
            .imageMainUrl("https://ticketrush.com/image.png")
            .build();

    mockMvc
        .perform(
            post("/api/v1/performance")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.isSuccess").value(true));
  }
}
