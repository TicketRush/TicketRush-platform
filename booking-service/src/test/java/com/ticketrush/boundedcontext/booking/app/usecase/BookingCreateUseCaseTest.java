package com.ticketrush.boundedcontext.booking.app.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.ticketrush.boundedcontext.booking.app.dto.request.BookingCreateRequest;
import com.ticketrush.boundedcontext.booking.app.mapper.BookingMapper;
import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import com.ticketrush.boundedcontext.booking.out.repository.BookingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingCreateUseCaseTest {

  @InjectMocks private BookingCreateUseCase bookingCreateUseCase;

  @Mock private BookingRepository bookingRepository;

  @Mock private BookingMapper bookingMapper;

  @Test
  @DisplayName("성공: 예약 요청을 받아 Entity로 변환 후 저장한다")
  void execute_success() {
    // given
    BookingCreateRequest request = new BookingCreateRequest(1L, 2L, 3L, "BOOK-1234");
    Booking mappedBooking = mock(Booking.class);
    Booking savedBooking = mock(Booking.class);

    given(bookingMapper.toEntity(request)).willReturn(mappedBooking);
    given(bookingRepository.save(mappedBooking)).willReturn(savedBooking);

    // when
    Booking result = bookingCreateUseCase.execute(request);

    // then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(savedBooking);
  }
}
