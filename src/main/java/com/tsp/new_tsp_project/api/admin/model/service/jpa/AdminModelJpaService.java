package com.tsp.new_tsp_project.api.admin.model.service.jpa;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import com.tsp.new_tsp_project.api.admin.model.service.impl.jpa.ModelRepository;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonCodeEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AdminModelJpaService {

	private final ModelRepository modelRepository;

	/**
	 * <pre>
	 * 1. MethodName : findModelsCount
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 */
	@Transactional(readOnly = true)
	public Long findModelsCount(Map<String, Object> modelMap) {
		return modelRepository.findModelsCount(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findModelsList
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 */
	@Transactional(readOnly = true)
	public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {
		return modelRepository.findModelsList(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneModel
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 */
	@Transactional
	public AdminModelDTO findOneModel(AdminModelEntity adminModelEntity) {
		return modelRepository.findOneModel(adminModelEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : modelCommonCode
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 공통 코드 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	@Transactional(readOnly = true)
	public ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity modelCodeEntity) {
		return modelRepository.modelCommonCode(modelCodeEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 */
	@Transactional
	public Integer insertModel(AdminModelEntity adminModelEntity) {
		return modelRepository.insertModel(adminModelEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : updateModel
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 */
	@Transactional
	public Integer updateModel(AdminModelEntity adminModelEntity, CommonImageEntity commonImageEntity,
							   MultipartFile[] files, ConcurrentHashMap<String, Object> modelMap) {

		int num;

		try {
			if(this.modelRepository.updateModel(adminModelEntity, commonImageEntity, files, modelMap) > 0) {
				commonImageEntity.setTypeName("model");
				commonImageEntity.setTypeIdx(adminModelEntity.getIdx());
				num = 1;
			} else {
				throw new TspException(ApiExceptionType.ERROR_MODEL);
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_MODEL);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteModel
	 * 2. ClassName  : AdminModelJpaService.java
	 * 3. Comment    : 관리자 모델 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 */
	@Transactional
	public Long deleteModel(AdminModelEntity adminModelEntity) {
		return this.modelRepository.deleteModel(adminModelEntity);
	}
}
