package com.tsp.new_tsp_project.api.admin.production.service.jpa;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;
import com.tsp.new_tsp_project.api.admin.production.service.impl.jpa.ProductionRepository;
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
public class AdminProductionJpaService {

	private final ProductionRepository productionRepository;

	/**
	 * <pre>
	 * 1. MethodName : findProductionCount
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param productionMap
	 */
	@Transactional(readOnly = true)
	public Long findProductionCount(ConcurrentHashMap<String, Object> productionMap) {
		return this.productionRepository.findProductionCount(productionMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findProductionList
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param productionMap
	 */
	@Transactional(readOnly = true)
	public List<AdminProductionDTO> findProductionList(Map<String, Object> productionMap) {
		return productionRepository.findProductionList(productionMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneProduction
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 */
	@Transactional(readOnly = true)
	public ConcurrentHashMap<String, Object> findOneProduction(AdminProductionEntity adminProductionEntity) {
		return productionRepository.findOneProduction(adminProductionEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : insertProduction
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@Transactional
	public Integer insertProduction(AdminProductionEntity adminProductionEntity,
									CommonImageEntity commonImageEntity,
									MultipartFile[] files) {
		return productionRepository.insertProduction(adminProductionEntity, commonImageEntity, files);
	}

	/**
	 * <pre>
	 * 1. MethodName : updateProduction
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @param commonImageEntity
	 * @param files
	 */
	public Integer updateProduction(AdminProductionEntity adminProductionEntity,
									CommonImageEntity commonImageEntity,
									MultipartFile[] files) {

		int num;

		try {
			if(this.productionRepository.updateProduction(adminProductionEntity, commonImageEntity, files) > 0) {
				commonImageEntity.setTypeName("production");
				commonImageEntity.setTypeIdx(adminProductionEntity.getIdx());
				num = 1;
			} else {
				throw new TspException(ApiExceptionType.ERROR_PRODUCTION);
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PRODUCTION);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteProduction
	 * 2. ClassName  : AdminProductionJpaService.java
	 * 3. Comment    : 관리자 프로덕션 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 */
	public Long deleteProduction(AdminProductionEntity adminProductionEntity) {
		return this.productionRepository.deleteProduction(adminProductionEntity);
	}
}
