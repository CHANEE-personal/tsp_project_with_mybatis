package com.tsp.new_tsp_project.api.admin.mapper;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminSupportMapper {

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelCnt
	 * 2. ClassName  : AdminProductionMapper.java
	 * 3. Comment    : 관리자 지원모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param searchMap
	 * @throws Exception
	 */
	Integer getSupportModelCnt(Map<String, Object> searchMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelList
	 * 2. ClassName  : AdminProductionMapper.java
	 * 3. Comment    : 관리자 지원모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param searchMap
	 * @throws Exception
	 */
	List<AdminSupportDTO> getSupportModelList(Map<String, Object> searchMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelInfo
	 * 2. ClassName  : AdminProductionMapper.java
	 * 3. Comment    : 관리자 지원모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param adminSupportDTO
	 * @throws Exception
	 */
	Map<String, Object> getSupportModelInfo(AdminSupportDTO adminSupportDTO) throws Exception;
}
