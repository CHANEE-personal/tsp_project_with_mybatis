package com.tsp.new_tsp_project.api.admin.mapper;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelNewDto;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminModelMapper {
    /**
     * <pre>
     * 1. MethodName : getModelListCnt
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    Integer getModelListCnt(Map<String, Object> modelMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : getModelList
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    List<AdminModelDTO> getModelList(Map<String, Object> modelMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : getModelInfo
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    AdminModelNewDto getModelInfo(AdminModelDTO adminModelDTO) throws Exception;

    /**
     * <pre>
     * 1. MethodName : getImageList
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 이미지 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    List<CommonImageDTO> getImageList(CommonImageDTO commonImageDTO) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 남자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06
     * </pre>
     */
    Integer insertModel(AdminModelDTO adminModelDTO) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 남자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06
     * </pre>
     */
    Integer updateModel(AdminModelDTO adminModelDTO) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06
     * </pre>
     */
    Integer deleteModelImage(CommonImageDTO commonImageDTO) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelMapper.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06
     * </pre>
     */
    Integer deleteModel(AdminModelDTO adminModelDTO) throws Exception;
}
