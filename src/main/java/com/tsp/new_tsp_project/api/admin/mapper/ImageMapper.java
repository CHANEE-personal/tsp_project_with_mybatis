package com.tsp.new_tsp_project.api.admin.mapper;

import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {

	/**
	 * <pre>
	 * 1. MethodName : selectSubCnt
	 * 2. ClassName  : ImageMapper.java
	 * 3. Comment    : 이미지 파일 최대 값 가져오기
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 18.
	 * </pre>
	 *
	 */
	Integer selectSubCnt(CommonImageDTO commonImageDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : addImageFile
	 * 2. ClassName  : ImageMapper.java
	 * 3. Comment    : 이미지 파일 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 18.
	 * </pre>
	 *
	 */
	Integer addImageFile(CommonImageDTO commonImageDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : deleteImageFile
	 * 2. ClassName  : ImageMapper.java
	 * 3. Comment    : 이미지 파일 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 18.
	 * </pre>
	 *
	 */
	Integer deleteImageFile(CommonImageDTO commonImageDTO) throws Exception;

}
