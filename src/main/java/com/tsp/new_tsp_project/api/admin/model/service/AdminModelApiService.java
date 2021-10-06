package com.tsp.new_tsp_project.api.admin.model.service;

import com.sun.org.apache.xpath.internal.operations.Mult;
import com.tsp.new_tsp_project.api.common.image.CommonImageDTO;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public interface AdminModelApiService {

	/**
	 * <pre>
	 * 1. MethodName : getModelListCnt
	 * 2. ClassName  : AdminModelApiService.java
	 * 3. Comment    : 관리자 모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	Integer getModelListCnt(Map<String, Object> modelMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getModelList
	 * 2. ClassName  : AdminModelApiService.java
	 * 3. Comment    : 관리자 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	List<AdminModelDTO> getModelList(Map<String, Object> modelMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : getModelInfo
	 * 2. ClassName  : AdminModelApiService.java
	 * 3. Comment    : 관리자 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @throws Exception
	 */
	ConcurrentHashMap<String, Object> getModelInfo(AdminModelDTO adminModelDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : AdminModelApiService.java
	 * 3. Comment    : 관리자 남자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 * @throws Exception
	 */
	Integer insertMenModel(AdminModelDTO adminModelDTO,
					   CommonImageDTO commonImageDTO,
					   MultipartFile[] fileName) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertWomenModel
	 * 2. ClassName  : AdminModelApiService.java
	 * 3. Comment    : 관리자 여자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 * @throws Exception
	 */
	Integer insertWomenModel(AdminModelDTO adminModelDTO,
							 CommonImageDTO commonImageDTO,
							 MultipartFile[] fileName) throws Exception;
}
