package com.stockInformation.cikLookup.api.v1;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.stockInformation.cikLookup.dto.CikLookupDTO;
import com.stockInformation.cikLookup.entity.CikLookup;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CikLookupMapper {

    CikLookupDTO toDTO(CikLookup entity);

    CikLookup toEntity(CikLookupDTO dto);

    List<CikLookupDTO> toDTOList(List<CikLookup> entities);

    void updateEntityFromDTO(CikLookupDTO dto, @MappingTarget CikLookup entity);
}
