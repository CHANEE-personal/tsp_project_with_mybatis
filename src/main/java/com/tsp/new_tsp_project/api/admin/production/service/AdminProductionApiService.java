package com.tsp.new_tsp_project.api.admin.production.service;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public interface AdminProductionApiService {

	/**
	 * <pre>
	 * 1. MethodName : getProductionCnt
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	Integer getProductionCnt(ConcurrentHashMap<String, Object> searchMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getProductionList
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	List<AdminProductionDTO> getProductionList(ConcurrentHashMap<String, Object> searchMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getProductionInfo
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	ConcurrentHashMap<String, Object> getProductionInfo(AdminProductionDTO adminProductionDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertProduction
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	Integer insertProduction(AdminProductionDTO adminProductionDTO,
							 CommonImageDTO commonImageDTO,
							 MultipartFile[] files) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : updateProduction
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	Integer updateProduction(AdminProductionDTO adminProductionDTO,
							 CommonImageDTO commonImageDTO,
							 MultipartFile[] files) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : deleteProduction
	 * 2. ClassName  : AdminProductionApiService.java
	 * 3. Comment    : 관리자 프로덕션 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	Integer deleteProduction(AdminProductionDTO adminProductionDTO) throws Exception;
}
