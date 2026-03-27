package com.ticketrush.global.response;

public record PageInfo(
    int currentPage, int size, boolean hasNext, long totalElements, int totalPages)
    implements PaginationInfo {}
