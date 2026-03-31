package com.ticketrush.boundedcontext.booking.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookingNumberGeneratorTest {

  @Test
  @DisplayName("예약 번호는 총 11자리(하이픈 포함)이며, 오입력 방지 문자가 제외된 포맷이어야 한다.")
  void testBookingNumberFormat() {
    // given & when
    String bookingNumber = BookingNumberGenerator.generate();

    // then
    assertThat(bookingNumber).hasSize(11);
    assertThat(bookingNumber.charAt(5)).isEqualTo('-');

    // 정규식 검증: 0, 1, O, I, L이 제외된 대문자 및 숫자 조합인지 확인
    assertThat(bookingNumber).matches("^[2-9A-HJ-NP-Z]{5}-[2-9A-HJ-NP-Z]{5}$");
  }

  @Test
  @DisplayName("생성된 예약 번호는 무작위성을 가져야 한다. (10,000번 생성 시 중복 없음)")
  void testBookingNumberRandomness() {
    // given
    int generateCount = 10000;
    Set<String> generatedNumbers = new HashSet<>();

    // when
    for (int i = 0; i < generateCount; i++) {
      generatedNumbers.add(BookingNumberGenerator.generate());
    }

    // then
    assertThat(generatedNumbers).hasSize(generateCount);
  }
}
