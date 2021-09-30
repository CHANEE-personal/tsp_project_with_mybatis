package com.tsp.new_tsp_project.api.common.image.service.impl;

import com.tsp.new_tsp_project.api.common.image.CommonImageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {

	/**
	 * <pre>
	 * 1. MethodName : addImageFile
	 * 2. ClassName  : ImageMapper.java
	 * 3. Comment    : 이미지 파일 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 18.
	 * </pre>
	 *
	 * @param commonImageDTO
	 * @return
	 * @throws Exception
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
	 * @param commonImageDTO
	 * @return
	 * @throws Exception
	 */
	Integer deleteImageFile(CommonImageDTO commonImageDTO) throws Exception;

}
