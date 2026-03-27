package com.ticketrush.global.dto.request;

import static com.ticketrush.global.constants.PaginationConstants.DEFAULT_PAGE_SIZE;

public record CursorPageRequest(Long cursorId, Integer size) implements PaginationRequest {

  // 컴팩트 생성자를 통한 기본값 설정
  public CursorPageRequest {
    if (size == null || size < 1) {
      size = DEFAULT_PAGE_SIZE;
    }
  }
}
