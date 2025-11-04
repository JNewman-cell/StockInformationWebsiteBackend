package com.stockinfo.backend.api.v1.cik;

import com.stockinfo.backend.entity.CikLookup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CikLookupMapper {

    CikLookupDTO toDTO(CikLookup entity);

    CikLookup toEntity(CikLookupDTO dto);

    List<CikLookupDTO> toDTOList(List<CikLookup> entities);

    void updateEntityFromDTO(CikLookupDTO dto, @MappingTarget CikLookup entity);
}
