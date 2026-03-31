package com.ticketrush.boundedcontext.performance.app.mapper;

import com.ticketrush.boundedcontext.performance.app.dto.request.PerformanceCreateRequest;
import com.ticketrush.boundedcontext.performance.app.dto.response.PerformanceCreateResponse;
import com.ticketrush.boundedcontext.performance.domain.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PerformanceMapper {

  @Mapping(target = "performanceStatus", constant = "UPCOMING")
  Performance toEntity(PerformanceCreateRequest request);

  @Mapping(source = "id", target = "performanceId")
  PerformanceCreateResponse toCreateResponse(Performance performance);
}
