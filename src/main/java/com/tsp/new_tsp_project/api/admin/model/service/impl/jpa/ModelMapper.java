package com.tsp.new_tsp_project.api.admin.model.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import com.tsp.new_tsp_project.common.mapStruct.StructMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ModelMapper extends StructMapper<AdminModelDTO, AdminModelEntity> {

	ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

	@Override
	AdminModelDTO toDto(AdminModelEntity entity);

	@Override
	AdminModelEntity toEntity(AdminModelDTO dto);

	@Override
	List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList);

	@Override
	List<AdminModelEntity> toEntityList(List<AdminModelDTO> dtoList);

}
