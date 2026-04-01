package com.ticketrush.boundedcontext.performance.app.mapper;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.domain.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {

  Performance toEntity(PerformanceCreateRequest request);

  @Mapping(source = "id", target = "performanceId")
  PerformanceCreateResponse toCreateResponse(Performance performance);
}
