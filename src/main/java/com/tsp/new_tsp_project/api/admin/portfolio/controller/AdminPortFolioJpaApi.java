package com.tsp.new_tsp_project.api.admin.portfolio.controller;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.admin.portfolio.service.jpa.AdminPortFolioJpaService;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonCodeEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/jpa-portfolio")
@Api(tags = "포트폴리오 관련 API")
public class AdminPortFolioJpaApi {

	private final AdminPortFolioJpaService adminPortFolioJpaService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : getPortFolioList
	 * 2. ClassName  : AdminPortFolio.java
	 * 3. Comment    : 관리자 포트폴리오 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param page
	 */
	@ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists")
	public ConcurrentHashMap getPortFolioList(Page page, @RequestParam(required = false) Map<String, Object> paramMap) {
		ConcurrentHashMap<String, Object> portFolioMap = new ConcurrentHashMap<>();

		ConcurrentHashMap<String, Object> searchMap = searchCommon.searchCommon(page, paramMap);

		Long portFolioCnt = this.adminPortFolioJpaService.findPortFolioCount(searchMap);

		List<AdminPortFolioDTO> portFolioList = new ArrayList<>();

		if (portFolioCnt > 0) {
			portFolioList = this.adminPortFolioJpaService.findPortFolioList(searchMap);
		}

		// 리스트 수
		portFolioMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		portFolioMap.put("perPageListCnt", Math.ceil((portFolioCnt - 1) / page.getSize() + 1));
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
	 *
	 * @param idx
	 */
	@ApiOperation(value = "포트폴리오 상세 조회", notes = "포트폴리오를 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/{idx}")
	public AdminPortFolioDTO getPortFolioInfo(@PathVariable("idx") Integer idx) {

		AdminPortFolioEntity adminPortFolioEntity = builder().idx(idx).build();

		return this.adminPortFolioJpaService.findOnePortFolio(adminPortFolioEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : portFolioCommonCode
	 * 2. ClassName  : AdminPortFolioJpaApi.java
	 * 3. Comment    : 관리자 포트폴리오 공통 코드 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 */
	@ApiOperation(value = "포트폴리오 공통 코드 조회", notes = "포트폴리오 공통 코드를 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "포트폴리오 공통 코드 조회 성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/common")
	public ConcurrentHashMap<String, Object> portFolioCommonCode() {
		ConcurrentHashMap<String, Object> portFolioMap = new ConcurrentHashMap<>();

		CommonCodeEntity portFolioCodeEntity = CommonCodeEntity.builder().cmmType("portFolio").build();

		portFolioMap.put("portFolioCmmCode", this.adminPortFolioJpaService.portFolioCommonCode(portFolioCodeEntity));

		return portFolioMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertPortFolio
	 * 2. ClassName  : AdminPortFolio.java
	 * 3. Comment    : 관리자 포트폴리오 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@ApiOperation(value = "포트폴리오 등록", notes = "포트폴리오를 등록한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String insertPortFolio(@Valid AdminPortFolioEntity adminPortFolioEntity,
								  CommonImageEntity commonImageEntity,
								  @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) {
		String result;

		if (this.adminPortFolioJpaService.insertPortFolio(adminPortFolioEntity, commonImageEntity, files) > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : updatePortFolio
	 * 2. ClassName  : AdminPortFolioJpaApi.java
	 * 3. Comment    : 관리자 포트폴리오 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@ApiOperation(value = "포트폴리오 수정", notes = "포트폴리오를 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/{idx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String updatePortFolio(@Valid AdminPortFolioEntity adminPortFolioEntity,
								  CommonImageEntity commonImageEntity,
								  HttpServletRequest request,
								  @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) {

		ConcurrentHashMap<String, Object> portFolioMap = new ConcurrentHashMap<>();

		String[] arrayState = request.getParameter("imageState").split(",");
		String[] arrayIdx = request.getParameter("idxState").split(",");

		portFolioMap.put("arrayState", arrayState);
		portFolioMap.put("arrayIdx", arrayIdx);

		String result = "N";

		if (this.adminPortFolioJpaService.updatePortFolio(adminPortFolioEntity, commonImageEntity, files, portFolioMap) > 0) {
			result = "Y";
		} else {
			result = "N";
		}

		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : deletePortFolio
	 * 2. ClassName  : AdminPortFolioJpaApi.java
	 * 3. Comment    : 관리자 포트폴리오 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 28.
	 * </pre>
	 * @param deleteIdx
	 */
	@ApiOperation(value = "포트폴리오 삭제", notes = "포트폴리오를 삭제한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@DeleteMapping("/deletePortfolio")
	public String deleteAllPortFolio(Long[] deleteIdx) {
		Map<String, Object> portFolioMap = new HashMap<>();
		String result;

		portFolioMap.put("deleteIdx", deleteIdx);

		if (this.adminPortFolioJpaService.deletePortFolio(portFolioMap) > 0) {
			result = "Y";
		} else {
			result = "N";
		}

		return result;
	}
}
