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

  // Performance 모듈 에러
  PERFORMANCE_MAIN_IMAGE_MISSING(HttpStatus.BAD_REQUEST, "PERFORMANCE_400_001", "메인 이미지는 필수입니다."),
  PERFORMANCE_MODEL_3D_MISSING(HttpStatus.BAD_REQUEST, "PERFORMANCE_400_002", "3D 모델 파일은 필수입니다."),
  PERFORMANCE_GALLERY_LIMIT_EXCEEDED(
      HttpStatus.BAD_REQUEST, "PERFORMANCE_400_003", "갤러리 이미지는 최대 3개까지 업로드할 수 있습니다."),

  // 파일 업로드 공통 에라
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
