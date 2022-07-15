package com.tsp.new_tsp_project.api.common.image.service;

import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ImageService {

    /**
     * <pre>
     * 1. MethodName : uploadImageFile
     * 2. ClassName  : ImageService.java
     * 3. Comment    : 이미지 파일 업로드
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 06. 02.
     * </pre>
     */
    String uploadImageFile(CommonImageDTO commonImageDTO,
                           List<MultipartFile> files,
                           String flag) throws Exception;

    String updateMultipleFile(CommonImageDTO commonImageDTO, MultipartFile[] files, Map<String, Object> modelMap) throws Exception;
}
