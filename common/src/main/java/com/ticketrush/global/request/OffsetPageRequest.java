package com.ticketrush.global.request;

public record OffsetPageRequest(Integer page, Integer size) implements PaginationRequest {

  public OffsetPageRequest {
    if (page == null || page < 0) {
      page = 0;
    }
    if (size == null || size < 1) {
      size = 10;
    }
  }
}
