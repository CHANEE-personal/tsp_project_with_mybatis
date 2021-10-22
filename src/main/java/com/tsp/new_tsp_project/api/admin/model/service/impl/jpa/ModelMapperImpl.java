package com.tsp.new_tsp_project.api.admin.model.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModelMapperImpl implements ModelMapper {

	@Override
	public AdminModelDTO toDto(AdminModelEntity entity) {
		if (entity == null) {
			return null;
		}

		AdminModelDTO adminModelDTO = AdminModelDTO.builder().idx(entity.getIdx())
				.rnum(entity.getRnum())
				.categoryCd(entity.getCategoryCd())
				.modelKorName(entity.getModelKorName())
				.modelEngName(entity.getModelEngName())
				.modelDescription(entity.getModelDescription())
				.visible(entity.getVisible())
				.height(entity.getHeight())
				.shoes(entity.getShoes())
				.size3(entity.getSize3())
				.categoryAge(entity.getCategoryAge())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.updater(entity.getUpdater())
				.updateTime(entity.getUpdateTime())
				.build();


		return adminModelDTO;
	}

	@Override
	public AdminModelEntity toEntity(AdminModelDTO dto) {

		if(dto == null) {
			return null;
		}

		AdminModelEntity adminModelEntity = AdminModelEntity.builder()
				.rnum(dto.getRnum())
				.idx(dto.getIdx())
				.categoryCd(dto.getCategoryCd())
				.modelKorName(dto.getModelKorName())
				.modelEngName(dto.getModelEngName())
				.modelDescription(dto.getModelDescription())
				.visible(dto.getVisible())
				.height(dto.getHeight())
				.shoes(dto.getShoes())
				.size3(dto.getSize3())
				.categoryAge(dto.getCategoryAge())
				.creator(dto.getCreator())
				.createTime(dto.getCreateTime())
				.updater(dto.getUpdater())
				.updateTime(dto.getUpdateTime())
				.build();

		return adminModelEntity;
	}

	@Override
	public List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList) {

		if(entityList == null) {
			return null;
		}

		List<AdminModelDTO> list = new ArrayList<>(entityList.size());
		for(AdminModelEntity adminModelEntity : entityList) {
			list.add(toDto(adminModelEntity));
		}

		return list;
	}

	@Override
	public List<AdminModelEntity> toEntityList(List<AdminModelDTO> dtoList) {

		if(dtoList == null) {
			return null;
		}

		List<AdminModelEntity> list = new ArrayList<>(dtoList.size());
		for(AdminModelDTO adminModelDTO : dtoList) {
			list.add(toEntity(adminModelDTO));
		}

		return list;
	}
}
