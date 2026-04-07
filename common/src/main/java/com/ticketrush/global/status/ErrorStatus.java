package com.ticketrush.global.status;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 관리자에게 문의 바랍니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401", "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청입니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_404", "페이지를 찾을 수 없습니다."),
  // 입력값 검증 관련 에러
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALID_400", "입력값이 올바르지 않습니다."),
  // Json 변환 관련 에러
  JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON_501", "데이터 변환 중 오류가 발생했습니다."),

  // Auth 403
  AUTH_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_403_001", "접근 권한이 없습니다."),

  // Booking 404
  BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING_404_001", "해당 예매를 찾을 수 없습니다."),

  // Booking 409
  BOOKING_CANCEL_NOT_ALLOWED(HttpStatus.CONFLICT, "BOOKING_409_001", "취소할 수 없는 예매 상태입니다."),

  // Booking 500
  BOOKING_NUMBER_RETRY_EXCEEDED(
      HttpStatus.INTERNAL_SERVER_ERROR, "BOOKING_500_001", "재시도 횟수를 초과하여 고유한 예약 번호를 생성할 수 없습니다."),

  // Seat 404
  SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT_404_001", "해당 좌석을 찾을 수 없습니다."),

  // Seat 409
  SEAT_NOT_AVAILABLE(HttpStatus.CONFLICT, "SEAT_409_001", "현재 예매 가능한 좌석이 아닙니다."),
  SEAT_ALREADY_LOCKED(HttpStatus.CONFLICT, "SEAT_409_002", "이미 다른 사용자가 결제를 진행 중인 좌석입니다."),
  
  // Performance 400
  PERFORMANCE_MAIN_IMAGE_MISSING(HttpStatus.BAD_REQUEST, "PERFORMANCE_400_001", "메인 이미지는 필수입니다."),
  PERFORMANCE_MODEL_3D_MISSING(HttpStatus.BAD_REQUEST, "PERFORMANCE_400_002", "3D 모델 파일은 필수입니다."),
  PERFORMANCE_GALLERY_LIMIT_EXCEEDED(
      HttpStatus.BAD_REQUEST, "PERFORMANCE_400_003", "갤러리 이미지는 최대 3개까지 업로드할 수 있습니다."),

  // File 400
  FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE_400_001", "업로드할 파일이 비어있습니다."),
  FILE_INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "FILE_400_002", "파일 확장자가 올바르지 않습니다."),
  FILE_EXTENSION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "FILE_400_003", "허용되지 않은 파일 형식입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  public String getMessage(String message) {
    return Optional.ofNullable(message)
        .filter(Predicate.not(String::isBlank))
        .orElse(this.getMessage());
  }
}
