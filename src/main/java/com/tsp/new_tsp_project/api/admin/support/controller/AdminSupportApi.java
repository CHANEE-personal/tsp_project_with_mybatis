package com.tsp.new_tsp_project.api.admin.support.controller;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import com.tsp.new_tsp_project.api.admin.support.service.AdminSupportService;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.web.client.HttpClientErrorException.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
@Api(tags = "지원모델관련 API")
public class AdminSupportApi {
    private final AdminSupportService adminSupportService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getSupportModelList
     * 2. ClassName  : AdminSupportApi.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 26.
     * </pre>
     */
    @ApiOperation(value = "지원모델 조회", notes = "지원모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/lists")
    public Map<String, Object> getSupportModelList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {

        Map<String, Object> supportMap = new HashMap<>();

        //페이징 및 조회조건
        Map<String, Object> searchMap = searchCommon.searchCommon(page, paramMap);

        Integer supportModelCnt = this.adminSupportService.getSupportModelCnt(searchMap);

        List<AdminSupportDTO> supportModelList = new ArrayList<>();

        if (supportModelCnt > 0) {
            supportModelList = this.adminSupportService.getSupportModelList(searchMap);
        }

        // 리스트 수
        supportMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        supportMap.put("perPageListCnt", ceil((supportModelCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        supportMap.put("supportListCnt", supportModelCnt);

        supportMap.put("supportModelList", supportModelList);

        return supportMap;
    }

    /**
     * <pre>
     * 1. MethodName : getSupportModelInfo
     * 2. ClassName  : AdminSupportApi.java
     * 3. Comment    : 관리자 지원모델 리스트 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 26.
     * </pre>
     */
    @ApiOperation(value = "지원모델 상세 조회", notes = "지원모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public Map<String, Object> getSupportModelInfo(@PathVariable Integer idx) throws Exception {
        Map<String, Object> supportMap = new HashMap<>();

        supportMap.put("supportModelInfo", this.adminSupportService.getSupportModelInfo(AdminSupportDTO.builder().idx(idx).build()));

        return supportMap;
    }
}
