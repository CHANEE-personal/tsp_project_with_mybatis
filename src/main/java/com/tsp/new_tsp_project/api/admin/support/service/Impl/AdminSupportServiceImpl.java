package com.tsp.new_tsp_project.api.admin.support.service.Impl;

import com.tsp.new_tsp_project.api.admin.mapper.AdminSupportMapper;
import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import com.tsp.new_tsp_project.api.admin.support.service.AdminSupportService;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("AdminSupportService")
@RequiredArgsConstructor
public class AdminSupportServiceImpl implements AdminSupportService {

	private final AdminSupportMapper adminSupportMapper;

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelCnt
	 * 2. ClassName  : AdminSupportService.java
	 * 3. Comment    : 관리자 지원모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 */
	@Override
	public Integer getSupportModelCnt(Map<String, Object> searchMap) {
		try {
			return this.adminSupportMapper.getSupportModelCnt(searchMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelList
	 * 2. ClassName  : AdminSupportService.java
	 * 3. Comment    : 관리자 지원모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 */
	@Override
	public List<AdminSupportDTO> getSupportModelList(Map<String, Object> searchMap) {
		try {
			return this.adminSupportMapper.getSupportModelList(searchMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getSupportModelInfo
	 * 2. ClassName  : AdminSupportService.java
	 * 3. Comment    : 관리자 지원모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 */
	@Override
	public Map<String, Object> getSupportModelInfo(AdminSupportDTO adminSupportDTO) {

		try {
			Map<String, Object> supportMap = new HashMap<>();

			supportMap.put("supportModelInfo", this.adminSupportMapper.getSupportModelInfo(adminSupportDTO));

			return supportMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT);
		}
	}
}
