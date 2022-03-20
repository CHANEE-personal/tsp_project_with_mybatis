package com.tsp.new_tsp_project.api.admin.portfolio.service.jpa;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.admin.portfolio.service.Impl.jpa.PortFolioRepository;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonCodeEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AdminPortFolioJpaService {

	private final PortFolioRepository portFolioRepository;

	/**
	 * <pre>
	 * 1. MethodName : findPortFolioCount
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param searchMap
	 */
	@Transactional(readOnly = true)
	public Long findPortFolioCount(ConcurrentHashMap<String, Object> searchMap) {
		return this.portFolioRepository.findPortFolioCount(searchMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : findPortFolioList
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	@Transactional(readOnly = true)
	public List<AdminPortFolioDTO> findPortFolioList(Map<String, Object> portFolioMap) {
		return portFolioRepository.findPortFolioList(portFolioMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : portFolioCommonCode
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 공통 코드 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@Transactional(readOnly = true)
	public ConcurrentHashMap<String, Object> portFolioCommonCode(CommonCodeEntity modelCodeEntity) {
		return portFolioRepository.portFolioCommonCode(modelCodeEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : findOnePortFolio
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminPortFolioEntity
	 */
	@Transactional(readOnly = true)
	public AdminPortFolioDTO findOnePortFolio(AdminPortFolioEntity adminPortFolioEntity) {
		return portFolioRepository.findOnePortFolio(adminPortFolioEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : insertPortFolio
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminPortFolioEntity
	 */
	@Transactional
	public Integer insertPortFolio(AdminPortFolioEntity adminPortFolioEntity, CommonImageEntity commonImageEntity, MultipartFile[] files) {
		return portFolioRepository.insertPortFolio(adminPortFolioEntity, commonImageEntity, files);
	}

	/**
	 * <pre>
	 * 1. MethodName : updatePortFolio
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06
	 * </pre>
	 *
	 * @param adminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@Modifying
	@Transactional
	public Integer updatePortFolio(AdminPortFolioEntity adminPortFolioEntity,
								   CommonImageEntity commonImageEntity,
								   MultipartFile[] files, ConcurrentHashMap<String, Object> portFolioMap) {
		int num;

		try {
			if(this.portFolioRepository.updatePortFolio(adminPortFolioEntity, commonImageEntity, files, portFolioMap) > 0) {
				CommonImageDTO.builder().typeIdx(adminPortFolioEntity.getIdx()).typeName("portfolio").build();
				num = 1;
			} else {
				throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deletePortFolio
	 * 2. ClassName  : AdminPortFolioJpaService.java
	 * 3. Comment    : 관리자 포트폴리오 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 28.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	@Transactional
	public Long deletePortFolio(Map<String, Object> portFolioMap) {
		return portFolioRepository.deletePortFolio(portFolioMap);
	}
}
