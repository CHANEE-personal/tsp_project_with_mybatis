package com.tsp.new_tsp_project.api.admin.model.service.impl;

import com.tsp.new_tsp_project.api.admin.model.service.AdminModelApiService;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.image.service.ImageService;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("AdminModelApiService")
public class AdminModelApiServiceImpl implements AdminModelApiService {

	private final AdminModelMapper adminModelMapper;
	private final ImageService imageService;

	/**
	 * <pre>
	 * 1. MethodName : getModelListCnt
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	@Override
	public Integer getModelListCnt(Map<String, Object> modelMap) throws Exception {
		return this.adminModelMapper.getModelListCnt(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelList
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 */
	@Override
	public List<AdminModelDTO> getModelList(Map<String, Object> modelMap) {
		try {
			return this.adminModelMapper.getModelList(modelMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelInfo
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelDTO
	 */
	@Override
	public ConcurrentHashMap<String, Object> getModelInfo(AdminModelDTO adminModelDTO) {
		try {
			ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();

			CommonImageDTO commonImageDTO = CommonImageDTO.builder()
					.typeIdx(adminModelDTO.getIdx())
					.typeName("model")
					.build();

			modelMap.put("modelInfo", this.adminModelMapper.getModelInfo(adminModelDTO));
			modelMap.put("modelImageList", this.adminModelMapper.getImageList(commonImageDTO));

			return modelMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 */
	public Integer insertModel(AdminModelDTO adminModelDTO,
							  CommonImageDTO commonImageDTO,
							  MultipartFile[] fileName) {
		int num;

		try {
			if(this.adminModelMapper.insertModel(adminModelDTO) > 0) {
				commonImageDTO.setTypeName("model");
				commonImageDTO.setTypeIdx(adminModelDTO.getIdx());
				commonImageDTO.setVisible("Y");
//				commonImageDTO.builder().typeName("model").typeIdx(adminModelDTO.getIdx()).visible("Y").build();
				if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, fileName, "insert"))) {
					num = 1;
				} else {
					throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
				}
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
	 * 1. MethodName : updateModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 */
	public Integer updateModel(AdminModelDTO adminModelDTO,
							   CommonImageDTO commonImageDTO,
							   MultipartFile[] fileName,
							   Map<String, Object> modelMap) {
		int num;

		try {
			if(this.adminModelMapper.updateModel(adminModelDTO) > 0) {
				CommonImageDTO.builder().typeName("model").typeIdx(adminModelDTO.getIdx()).visible("Y").build();
				if("Y".equals(this.imageService.updateMultipleFile(commonImageDTO, fileName, modelMap))) {
					num = 1;
				} else {
					throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
				}
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
	 * 1. MethodName : deleteModelImage
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 이미지 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06
	 * </pre>
	 *
	 * @param commonImageDTO
	 * @throws Exception
	 */
	public Integer deleteModelImage(CommonImageDTO commonImageDTO) throws Exception {
		return this.adminModelMapper.deleteModelImage(commonImageDTO);
	}


	/**
	 * <pre>
	 * 1. MethodName : deleteModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 모델 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @throws Exception
	 */
	public Integer deleteModel(AdminModelDTO adminModelDTO) throws Exception {
		return this.adminModelMapper.deleteModel(adminModelDTO);
	}
}
