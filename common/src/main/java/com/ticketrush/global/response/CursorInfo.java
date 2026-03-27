package com.ticketrush.global.response;

public record CursorInfo(boolean hasNext, Long nextCursor, int size) implements PaginationInfo {}
