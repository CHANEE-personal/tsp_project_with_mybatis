package com.tsp.new_tsp_project.api.admin.portfolio.controller;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.service.AdminPortFolioApiService;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.client.HttpClientErrorException.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/portfolio")
@Api(tags = "포트폴리오 관련 API")
public class AdminPortFolioApi {
    private final AdminPortFolioApiService adminPortFolioApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getPortFolioList
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> getPortFolioList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        Map<String, Object> portFolioMap = new HashMap<>();

        Map<String, Object> searchMap = searchCommon.searchCommon(page, paramMap);

        Integer portFolioCnt = this.adminPortFolioApiService.getPortFolioCnt(searchMap);

        List<AdminPortFolioDTO> portFolioList = new ArrayList<>();

        if (portFolioCnt > 0) {
            portFolioList = this.adminPortFolioApiService.getPortFolioList(searchMap);
        }

        // 리스트 수
        portFolioMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        portFolioMap.put("perPageListCnt", ceil((portFolioCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        portFolioMap.put("portFolioListCnt", portFolioCnt);

        portFolioMap.put("portFolioList", portFolioList);

        return portFolioMap;
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioInfo
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 상세 조회", notes = "포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public Map<String, Object> getPortFolioInfo(@PathVariable Integer idx) throws Exception {
        return this.adminPortFolioApiService.getPortFolioInfo(AdminPortFolioDTO.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertPortFolio
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 등록", notes = "포트폴리오를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public String insertPortFolio(@Valid AdminPortFolioDTO adminPortFolioDTO,
                                  CommonImageDTO commonImageDTO,
                                  @RequestParam(value = "imageFiles", required = false) List<MultipartFile> files) throws Exception {
        String result;

        if (this.adminPortFolioApiService.insertPortFolio(adminPortFolioDTO, commonImageDTO, files) > 0) {
            result = "Y";
        } else {
            result = "N";
        }
        return result;
    }

    /**
     * <pre>
     * 1. MethodName : updatePortFolio
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 수정", notes = "포트폴리오를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}", consumes = MULTIPART_FORM_DATA_VALUE)
    public String updatePortFolio(@Valid AdminPortFolioDTO adminPortFolioDTO,
                                  CommonImageDTO commonImageDTO,
                                  HttpServletRequest request,
                                  @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) throws Exception {

        Map<String, Object> portFolioMap = new HashMap<>();

        String[] arrayState = request.getParameter("imageState").split(",");
        String[] arrayIdx = request.getParameter("idxState").split(",");

        portFolioMap.put("arrayState", arrayState);
        portFolioMap.put("arrayIdx", arrayIdx);

        String result;

        if (this.adminPortFolioApiService.updatePortFolio(adminPortFolioDTO, commonImageDTO, files, portFolioMap) > 0) {
            result = "Y";
        } else {
            result = "N";
        }

        return result;
    }

    /**
     * <pre>
     * 1. MethodName : deletePortFolio
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 28.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 삭제", notes = "포트폴리오를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public String deletePortFolio(@PathVariable Integer idx) throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder().idx(idx).build();

        String result;

        if (this.adminPortFolioApiService.deletePortFolio(adminPortFolioDTO) > 0) {
            result = "Y";
        } else {
            result = "N";
        }

        return result;
    }

    /**
     * <pre>
     * 1. MethodName : deleteAllPortFolio
     * 2. ClassName  : AdminPortFolio.java
     * 3. Comment    : 관리자 포트폴리오 전체 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 28.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 전체 삭제", notes = "포트폴리오를 전체 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/delete-portfolio")
    public String deleteAllPortFolio(String deleteIdx) throws Exception {
        Map<String, Object> portFolioMap = new HashMap<>();
        String result;

        String[] arrayIdx = deleteIdx.split(",");
        portFolioMap.put("arrayIdx", arrayIdx);

        if (this.adminPortFolioApiService.deletePartPortFolio(portFolioMap) > 0) {
            result = "Y";
        } else {
            result = "N";
        }

        return result;
    }
}
