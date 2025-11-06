package com.stockInformation.tickerSummary.api.v1;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.entity.TickerSummary;

import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TickerSummaryMapper {

    @Mapping(target = "cik", source = "cikLookup.cik")
    @Mapping(target = "companyName", source = "cikLookup.companyName")
    TickerSummaryDTO toDTO(TickerSummary entity);

    // When converting DTO to entity, we ignore cikLookup; service layer should resolve and set it.
    @Mapping(target = "cikLookup", ignore = true)
    TickerSummary toEntity(TickerSummaryDTO dto);

    List<TickerSummaryDTO> toDTOList(List<TickerSummary> entities);

    // Update existing entity from DTO
    @Mapping(target = "cikLookup", ignore = true)
    void updateEntityFromDTO(TickerSummaryDTO dto, @MappingTarget TickerSummary entity);
}
