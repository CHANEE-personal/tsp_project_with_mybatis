package com.tsp.new_tsp_project.api.admin.portfolio.service.Impl;

import com.tsp.new_tsp_project.api.admin.mapper.AdminPortFolioMapper;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.service.AdminPortFolioApiService;
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
@Service("AdminPortFolioApiService")
@Transactional
@RequiredArgsConstructor
public class AdminPortFolioApiServiceImpl implements AdminPortFolioApiService {
    private final AdminPortFolioMapper adminPortFolioMapper;
    private final ImageService imageService;

    /**
     * <pre>
     * 1. MethodName : getPortFolioCnt
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public Integer getPortFolioCnt(Map<String, Object> searchMap) throws TspException {
        try {
            return this.adminPortFolioMapper.getPortFolioCnt(searchMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioList
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public List<AdminPortFolioDTO> getPortFolioList(Map<String, Object> searchMap) throws TspException {
        try {
            return this.adminPortFolioMapper.getPortFolioList(searchMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioInfo
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public Map<String, Object> getPortFolioInfo(AdminPortFolioDTO adminPortFolioDTO) throws TspException {

        Map<String, Object> portFolioMap = new HashMap<>();

        try {
            CommonImageDTO commonImageDTO = CommonImageDTO.builder().typeIdx(adminPortFolioDTO.getIdx()).typeName("portFolio").build();

            portFolioMap.put("portFolioInfo", this.adminPortFolioMapper.getPortFolioInfo(adminPortFolioDTO));
            portFolioMap.put("portFolioImageList", this.adminPortFolioMapper.getImageList(commonImageDTO));

            return portFolioMap;
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPortFolio
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public Integer insertPortFolio(AdminPortFolioDTO adminPortFolioDTO,
                                   CommonImageDTO commonImageDTO,
                                   List<MultipartFile> files) throws TspException {

        Integer num;

        try {
            if (this.adminPortFolioMapper.insertPortFolio(adminPortFolioDTO) > 0) {
                CommonImageDTO.builder().typeName("portfolio").typeIdx(adminPortFolioDTO.getIdx()).visible("Y").build();
                if ("Y".equals(this.imageService.uploadImageFile(commonImageDTO, files, "insert"))) {
                    num = 1;
                } else {
                    throw new TspException(NOT_EXIST_IMAGE, new Throwable().getCause());
                }
            } else {
                throw new TspException(ERROR_PORTFOLIO, new Throwable().getCause());
            }
            return num;
        } catch (Exception e) {
            throw new TspException(ERROR_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortFolio
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public Integer updatePortFolio(AdminPortFolioDTO adminPortFolioDTO,
                                   CommonImageDTO commonImageDTO,
                                   MultipartFile[] files,
                                   Map<String, Object> portFolioMap) throws TspException {

        Integer num;

        try {
            if (this.adminPortFolioMapper.updatePortFolio(adminPortFolioDTO) > 0) {
                CommonImageDTO.builder().typeName("portfolio").typeIdx(adminPortFolioDTO.getIdx()).visible("Y").build();
                if ("Y".equals(this.imageService.updateMultipleFile(commonImageDTO, files, portFolioMap))) {
                    num = 1;
                } else {
                    throw new TspException(NOT_EXIST_IMAGE, new Throwable().getCause());
                }
            } else {
                throw new TspException(ERROR_PORTFOLIO, new Throwable().getCause());
            }
        } catch (Exception e) {
            throw new TspException(ERROR_PORTFOLIO, e);
        }
        return num;
    }

    /**
     * <pre>
     * 1. MethodName : deletePortFolio
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    public Integer deletePortFolio(AdminPortFolioDTO adminPortFolioDTO) throws TspException {
        try {
            return this.adminPortFolioMapper.deletePortFolio(adminPortFolioDTO);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAllPortFolio
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 전체 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 28.
     * </pre>
     */
    public Integer deleteAllPortFolio(Map<String, Object> portFolioMap) throws TspException {
        try {
            return this.adminPortFolioMapper.deleteAllPortFolio(portFolioMap);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePartPortFolio
     * 2. ClassName  : AdminPortFolioServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 부분 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 28.
     * </pre>
     */
    public Integer deletePartPortFolio(Map<String, Object> portFolioMap) throws TspException {
        try {
            return this.adminPortFolioMapper.deletePartPortFolio(portFolioMap);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PORTFOLIO, e);
        }
    }
}
