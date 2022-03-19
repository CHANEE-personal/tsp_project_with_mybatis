package com.tsp.new_tsp_project.api.admin.production.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductionMapperImpl implements ProductionMapper {

	@Override
	public AdminProductionDTO toDto(AdminProductionEntity entity) {
		if(entity == null) {
			return null;
		}

		return AdminProductionDTO.builder()
				.rnum(entity.getRnum())
				.idx(entity.getIdx())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.visible(entity.getVisible())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.updater(entity.getUpdater())
				.updateTime(entity.getUpdateTime())
				.productionImage(ProductionImageMapperImpl.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
				.build();
	}

	@Override
	public AdminProductionEntity toEntity(AdminProductionDTO dto) {
		if(dto == null) {
			return null;
		}

		return AdminProductionEntity.builder()
				.rnum(dto.getRnum())
				.idx(dto.getIdx())
				.title(dto.getTitle())
				.description(dto.getDescription())
				.visible(dto.getVisible())
				.creator(dto.getCreator())
				.createTime(dto.getCreateTime())
				.updater(dto.getUpdater())
				.updateTime(dto.getUpdateTime())
				.build();
	}

	@Override
	public List<AdminProductionDTO> toDtoList(List<AdminProductionEntity> entityList) {
		if(entityList == null) {
			return null;
		}

		List<AdminProductionDTO> list = new ArrayList<>(entityList.size());
		for(AdminProductionEntity adminProductionEntity : entityList) {
			list.add(toDto(adminProductionEntity));
		}

		return list;
	}

	@Override
	public List<AdminProductionEntity> toEntityList(List<AdminProductionDTO> dtoList) {
		if(dtoList == null) {
			return null;
		}

		List<AdminProductionEntity> list = new ArrayList<>(dtoList.size());
		for(AdminProductionDTO adminProductionDTO : dtoList) {
			list.add(toEntity(adminProductionDTO));
		}

		return list;
	}
}
