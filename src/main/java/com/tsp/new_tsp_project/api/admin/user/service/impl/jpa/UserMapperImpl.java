package com.tsp.new_tsp_project.api.admin.user.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.admin.user.entity.AdminUserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserMapperImpl implements UserMapper {

	@Override
	public AdminUserDTO toDto(AdminUserEntity entity) {
		if (entity == null) {
			return null;
		}

		return AdminUserDTO.builder()
				.idx(entity.getIdx())
				.userId(entity.getUserId())
				.password(entity.getPassword())
				.name(entity.getName())
				.email(entity.getEmail())
				.visible(entity.getVisible())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.updater(entity.getUpdater())
				.updateTime(entity.getUpdateTime())
				.build();
	}

	@Override
	public AdminUserEntity toEntity(AdminUserDTO dto) {
		if (dto == null) {
			return null;
		}

		return AdminUserEntity.builder()
				.idx(dto.getIdx())
				.userId(dto.getUserId())
				.password(dto.getPassword())
				.name(dto.getName())
				.email(dto.getEmail())
				.visible(dto.getVisible())
				.creator(dto.getCreator())
				.createTime(dto.getCreateTime())
				.updater(dto.getUpdater())
				.updateTime(dto.getUpdateTime())
				.build();
	}

	@Override
	public List<AdminUserDTO> toDtoList(List<AdminUserEntity> entityList) {
		if (entityList == null) {
			return null;
		}

		List<AdminUserDTO> list = new ArrayList<>(entityList.size());
		for (AdminUserEntity adminUserEntity : entityList) {
			list.add(toDto(adminUserEntity));
		}

		return list;
	}

	@Override
	public List<AdminUserEntity> toEntityList(List<AdminUserDTO> dtoList) {
		if (dtoList == null) {
			return null;
		}

		List<AdminUserEntity> list = new ArrayList<>(dtoList.size());
		for (AdminUserDTO adminUserDTO : dtoList) {
			list.add(toEntity(adminUserDTO));
		}

		return list;
	}
}
