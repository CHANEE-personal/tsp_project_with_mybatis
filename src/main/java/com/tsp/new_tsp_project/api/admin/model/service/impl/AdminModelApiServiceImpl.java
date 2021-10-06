package com.tsp.new_tsp_project.api.admin.model.service.impl;

import com.tsp.new_tsp_project.api.admin.model.service.AdminModelApiService;
import com.tsp.new_tsp_project.api.admin.model.service.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.image.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.image.service.ImageService;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("AdminModelApiService")
@Transactional
@RequiredArgsConstructor
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
	 * @throws Exception
	 */
	@Override
	public List<AdminModelDTO> getModelList(Map<String, Object> modelMap) throws Exception {
		return this.adminModelMapper.getModelList(modelMap);
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
	 * @throws Exception
	 */
	@Override
	public ConcurrentHashMap<String, Object> getModelInfo(AdminModelDTO adminModelDTO) throws Exception {
		ConcurrentHashMap modelMap = new ConcurrentHashMap();
		modelMap.put("modelInfo", this.adminModelMapper.getModelInfo(adminModelDTO));
		return modelMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
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
	public Integer insertModel(AdminModelDTO adminModelDTO,
							  CommonImageDTO commonImageDTO,
							  MultipartFile[] fileName) throws Exception {
		Integer num = 0;

		adminModelDTO.setCategoryCd("1");
		adminModelDTO.setCategoryNm("men");

		try {
			if(this.adminModelMapper.insertModel(adminModelDTO) > 0) {
				adminModelDTO.setModelIdx(adminModelDTO.getIdx());
				if(this.adminModelMapper.insertModelOpt(adminModelDTO) > 0) {
					if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, fileName, "insert"))) {
						num = 1;
					} else {
						throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
					}
				} else {
					throw new TspException(ApiExceptionType.ERROR_MODEL);
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
	 * 1. MethodName : updateMenModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 남자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06.
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 * @throws Exception
	 */
	public Integer updateMenModel(AdminModelDTO adminModelDTO,
							   CommonImageDTO commonImageDTO,
							   MultipartFile[] fileName) throws Exception {
		Integer num = 0;

		adminModelDTO.setCategoryCd("1");
		adminModelDTO.setCategoryNm("men");

		try {
			if(this.adminModelMapper.updateMenModel(adminModelDTO) > 0) {
				adminModelDTO.setModelIdx(adminModelDTO.getIdx());
				if(this.adminModelMapper.insertModelOpt(adminModelDTO) > 0) {
					if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, fileName, "update"))) {
						num = 1;
					} else {
						throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
					}
				} else {
					throw new TspException(ApiExceptionType.ERROR_MODEL);
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
	 * 1. MethodName : insertWomenModel
	 * 2. ClassName  : AdminModelApiServiceImpl.java
	 * 3. Comment    : 관리자 여자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 06
	 * </pre>
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param fileName
	 * @throws Exception
	 */

	public Integer insertWomenModel(AdminModelDTO adminModelDTO,
							   CommonImageDTO commonImageDTO,
							   MultipartFile[] fileName) throws Exception {
		Integer num = 0;

		adminModelDTO.setCategoryCd("2");
		adminModelDTO.setCategoryNm("women");

		try {
			if(this.adminModelMapper.insertModel(adminModelDTO) > 0) {
				adminModelDTO.setModelIdx(adminModelDTO.getIdx());
				if(this.adminModelMapper.insertModelOpt(adminModelDTO) > 0) {
					if("Y".equals(this.imageService.uploadImageFile(commonImageDTO, fileName, "insert"))) {
						num = 1;
					} else {
						throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
					}
				} else {
					throw new TspException(ApiExceptionType.ERROR_MODEL);
				}
			} else {
				throw new TspException(ApiExceptionType.ERROR_MODEL);
			}
			return num;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_MODEL);
		}
	}
}
