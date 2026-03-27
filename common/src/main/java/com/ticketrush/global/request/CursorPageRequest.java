package com.ticketrush.global.request;

public record CursorPageRequest(Long cursorId, Integer size) implements PaginationRequest {

  // 컴팩트 생성자를 통한 기본값 설정
  public CursorPageRequest {
    if (size == null || size < 1) {
      size = 10;
    }
  }
}
