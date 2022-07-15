package com.tsp.new_tsp_project.api.admin.model.controller;

import com.tsp.new_tsp_project.api.admin.model.service.AdminModelApiService;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO.*;
import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.client.HttpClientErrorException.*;

@Slf4j
@Validated
@RequestMapping(value = "/api/model")
@RestController
@RequiredArgsConstructor
@Api(tags = "모델관련 API")
public class AdminModelApi {

    private final AdminModelApiService adminModelApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getModelList
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    @ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/{categoryCd}")
    public Map<String, Object> getModelList(@PathVariable("categoryCd")
                                            @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                            @RequestParam(required = false) Map<String, Object> paramMap,
                                            Page page) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        // 페이징 및 검색
        Map<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        Integer modelListCnt = this.adminModelApiService.getModelListCnt(modelMap);

        List<AdminModelDTO> modelList = new ArrayList<>();

        if (modelListCnt > 0) {
            modelList = this.adminModelApiService.getModelList(modelMap);
        }

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((modelListCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        resultMap.put("modelListTotalCnt", modelListCnt);

        resultMap.put("modelList", modelList);

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : getModelEdit
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}")
    public Map<String, Object> getModelEdit(@PathVariable("categoryCd")
                                            @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                            @PathVariable("idx") Integer idx) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> modelMap;

        modelMap = this.adminModelApiService.getModelInfo(builder().idx(idx).categoryCd(categoryCd).build());

        resultMap.put("modelMap", modelMap);

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     */
    @ApiOperation(value = "모델 등록", notes = "모델을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public String insertModel(@Valid AdminModelDTO adminModelDTO,
                              CommonImageDTO commonImageDTO,
                              NewCommonDTO newCommonDTO,
                              HttpServletRequest request,
                              @RequestParam(value = "imageFiles", required = false) List<MultipartFile> files) throws Exception {

        String result;
        searchCommon.giveAuth(request, newCommonDTO);

        if (this.adminModelApiService.insertModel(adminModelDTO, commonImageDTO, files) > 0) {
            result = "Y";
        } else {
            result = "N";
        }

        return result;
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06.
     * </pre>
     */
    @ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{categoryCd}/{idx}", consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public String updateModel(@PathVariable(value = "idx") Integer idx,
                              @PathVariable(value = "categoryCd") Integer categoryCd,
                              @Valid AdminModelDTO adminModelDTO,
                              CommonImageDTO commonImageDTO,
                              NewCommonDTO newCommonDTO,
                              HttpServletRequest request,
                              @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) throws Exception {

        searchCommon.giveAuth(request, newCommonDTO);

        Map<String, Object> modelMap = new HashMap<>();

        modelMap.put("arrayState", request.getParameter("imageState").split(","));
        modelMap.put("arrayIdx", request.getParameter("idxState").split(","));

        builder().idx(idx).categoryCd(categoryCd).build();

        String result;

        if (this.adminModelApiService.updateModel(adminModelDTO, commonImageDTO, files, modelMap) > 0) {
            result = "Y";
        } else {
            result = "N";
        }

        return result;
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 첨부파일 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06.
     * </pre>
     */
    @ApiOperation(value = "모델 이미지 삭제", notes = "모델 이미지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/image/{idx}")
    public String deleteModelImage(@PathVariable(value = "idx") Integer idx) throws Exception {
        String result;

        if (this.adminModelApiService.deleteModelImage(CommonImageDTO.builder().idx(idx).build()) > 0) {
            result = "Y";
        } else {
            result = "N";
        }
        return result;
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelApi.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 10. 06.
     * </pre>
     */
    @ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public String deleteModel(@PathVariable(value = "idx") Integer idx) throws Exception {
        String result;

        if (this.adminModelApiService.deleteModel(AdminModelDTO.builder().visible("N").idx(idx).build()) > 0) {
            result = "Y";
        } else {
            result = "N";
        }
        return result;
    }
}
