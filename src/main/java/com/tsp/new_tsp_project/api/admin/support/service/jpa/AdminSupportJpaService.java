package com.tsp.new_tsp_project.api.admin.support.service.jpa;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import com.tsp.new_tsp_project.api.admin.support.domain.entity.AdminSupportEntity;
import com.tsp.new_tsp_project.api.admin.support.service.Impl.jpa.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AdminSupportJpaService {

	private final SupportRepository supportRepository;

	/**
	 * <pre>
	 * 1. MethodName : findSupportModelCount
	 * 2. ClassName  : AdminSupportJpaService.java
	 * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param supportMap
	 */
	@Transactional(readOnly = true)
	public Long findSupportModelCount(Map<String, Object> supportMap) {
		return this.supportRepository.findSupportModelCount(supportMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findSupportModelList
	 * 2. ClassName  : AdminSupportJpaService.java
	 * 3. Comment    : 관리자 지원모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param supportMap
	 */
	@Transactional(readOnly = true)
	public List<AdminSupportDTO> findSupportModelList(Map<String, Object> supportMap) {
		return this.supportRepository.findSupportModelList(supportMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneSupportModel
	 * 2. ClassName  : AdminSupportJpaService.java
	 * 3. Comment    : 관리자 지원모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param adminSupportEntity
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> findOneSupportModel(AdminSupportEntity adminSupportEntity) {
		return this.supportRepository.findOneSupportModel(adminSupportEntity);
	}
}
