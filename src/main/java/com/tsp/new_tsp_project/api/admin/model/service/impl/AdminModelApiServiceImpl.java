package com.tsp.new_tsp_project.api.admin.model.service.impl;

import com.tsp.new_tsp_project.api.admin.mapper.AdminModelMapper;
import com.tsp.new_tsp_project.api.admin.model.service.AdminModelApiService;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.image.service.ImageService;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.exception.ApiExceptionType.*;

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
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer getModelListCnt(Map<String, Object> modelMap) throws TspException {
		try {
			return this.adminModelMapper.getModelListCnt(modelMap);
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL_LIST, e);
		}
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
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AdminModelDTO> getModelList(Map<String, Object> modelMap) throws TspException {
		try {
			return this.adminModelMapper.getModelList(modelMap);
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL_LIST, e);
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
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getModelInfo(AdminModelDTO adminModelDTO) throws TspException {
		try {
			Map<String, Object> modelMap = new HashMap<>();

			CommonImageDTO commonImageDTO = CommonImageDTO.builder()
					.typeIdx(adminModelDTO.getIdx())
					.typeName("model")
					.build();

			modelMap.put("modelInfo", this.adminModelMapper.getModelInfo(adminModelDTO));
			modelMap.put("modelImageList", this.adminModelMapper.getImageList(commonImageDTO));

			return modelMap;
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL, e);
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
	 */
	public Integer insertModel(AdminModelDTO adminModelDTO,
							   CommonImageDTO commonImageDTO,
							   List<MultipartFile> fileName) throws TspException {
		Integer num;

		try {
			if(this.adminModelMapper.insertModel(adminModelDTO) > 0) {
				commonImageDTO.setTypeName("model");
				commonImageDTO.setTypeIdx(adminModelDTO.getIdx());
				commonImageDTO.setVisible("Y");
				if ("Y".equals(this.imageService.uploadImageFile(commonImageDTO, fileName, "insert"))) {
					num = 1;
				} else {
					throw new TspException(NOT_EXIST_IMAGE, new Throwable().getCause());
				}
			} else {
				throw new TspException(ERROR_MODEL, new Throwable().getCause());
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ERROR_MODEL, e);
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
	 */
	public Integer updateModel(AdminModelDTO adminModelDTO,
							   CommonImageDTO commonImageDTO,
							   MultipartFile[] fileName,
							   Map<String, Object> modelMap) throws TspException {
		Integer num;

		try {
			if(this.adminModelMapper.updateModel(adminModelDTO) > 0) {
				CommonImageDTO.builder().typeName("model").typeIdx(adminModelDTO.getIdx()).visible("Y").build();
				if("Y".equals(this.imageService.updateMultipleFile(commonImageDTO, fileName, modelMap))) {
					num = 1;
				} else {
					throw new TspException(NOT_EXIST_IMAGE, new Throwable().getCause());
				}
			} else {
				throw new TspException(ERROR_MODEL, new Throwable().getCause());
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ERROR_MODEL, e);
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
	 */
	public Integer deleteModelImage(CommonImageDTO commonImageDTO) throws TspException {
		try {
			return this.adminModelMapper.deleteModelImage(commonImageDTO);
		} catch (Exception e) {
			throw new TspException(ERROR_DELETE_MODEL, e);
		}
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
	 */
	public Integer deleteModel(AdminModelDTO adminModelDTO) throws TspException {
		try {
			return this.adminModelMapper.deleteModel(adminModelDTO);
		} catch (Exception e) {
			throw new TspException(ERROR_DELETE_MODEL, e);
		}
	}
}
