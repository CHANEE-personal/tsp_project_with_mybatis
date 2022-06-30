package com.tsp.new_tsp_project.api.admin.production.service.impl;

import com.tsp.new_tsp_project.api.admin.mapper.AdminProductionMapper;
import com.tsp.new_tsp_project.api.admin.production.service.AdminProductionApiService;
import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.image.service.ImageService;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("AdminProductionApiService")
@Transactional
@RequiredArgsConstructor
public class AdminProductionApiServiceImpl implements AdminProductionApiService {
	private final AdminProductionMapper adminProductionMapper;
	private final ImageService imageService;

	/**
	 * <pre>
	 * 1. MethodName : getProductionCnt
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Override
	public Integer getProductionCnt(Map<String, Object> searchMap) throws Exception {
		return this.adminProductionMapper.getProductionCnt(searchMap);
	}


	/**
	 * <pre>
	 * 1. MethodName : getProductionList
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Override
	public List<AdminProductionDTO> getProductionList(Map<String, Object> searchMap) {
		try {
			return this.adminProductionMapper.getProductionList(searchMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION_LIST, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionInfo
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Override
	public Map<String, Object> getProductionInfo(AdminProductionDTO adminProductionDTO) {
		try {
			Map<String, Object> productionMap = new HashMap<>();

			productionMap.put("productionInfo", this.adminProductionMapper.getProductionInfo(adminProductionDTO));

			return productionMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : insertProduction
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Override
	public Integer insertProduction(AdminProductionDTO adminProductionDTO,
									CommonImageDTO commonImageDTO,
									MultipartFile[] files) {
		int num;

		try {
			if(this.adminProductionMapper.insertProduction(adminProductionDTO) > 0) {
				commonImageDTO.setTypeName("production");
				commonImageDTO.setTypeIdx(adminProductionDTO.getIdx());
				commonImageDTO.setVisible("Y");
				if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, files, "insert"))) {
					num = 1;
				} else {
					throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE, new Throwable().getCause());
				}
			} else {
				throw new TspException(ApiExceptionType.ERROR_PRODUCTION, new Throwable().getCause());
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PRODUCTION, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : updateProduction
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Override
	public Integer updateProduction(AdminProductionDTO adminProductionDTO,
									CommonImageDTO commonImageDTO,
									MultipartFile[] files) {
		int num;

		try {
			if(this.adminProductionMapper.updateProduction(adminProductionDTO) > 0) {
				CommonImageDTO.builder().typeName("production").typeIdx(adminProductionDTO.getIdx()).visible("Y").build();
				if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, files, "update"))) {
					num = 1;
				} else {
					throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE, new Throwable().getCause());
				}
			} else {
				throw new TspException(ApiExceptionType.ERROR_PRODUCTION, new Throwable().getCause());
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PRODUCTION, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteProduction
	 * 2. ClassName  : AdminProductionApiServiceImpl.java
	 * 3. Comment    : 관리자 프로덕션 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 05
	 * </pre>
	 *
	 */
	@Override
	public Integer deleteProduction(AdminProductionDTO adminProductionDTO) {
		try {
			return this.adminProductionMapper.deleteProduction(adminProductionDTO);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_DELETE_PRODUCTION, e);
		}
	}
}
