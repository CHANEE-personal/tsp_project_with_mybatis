package com.tsp.new_tsp_project.api.admin.production.service.impl.jpa;

import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.common.mapStruct.StructMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductionImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity> {

    ProductionImageMapper INSTANCE = Mappers.getMapper(ProductionImageMapper.class);

    @Override
    CommonImageDTO toDto(CommonImageEntity entity);

    @Override
    CommonImageEntity toEntity(CommonImageDTO dto);

    @Override
    List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

    @Override
    List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}