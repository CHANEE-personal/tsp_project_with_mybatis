package com.tsp.new_tsp_project.api.admin.model.controller;

import com.tsp.new_tsp_project.api.admin.model.service.AdminModelApiService;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.common.paging.Page;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.json.simple.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO.*;

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
	 * 1. MethodName : lists
	 * 2. ClassName  : AdminModelApi.java
	 * 3. Comment    : 관리자 모델 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param categoryCd
	 * @param paramMap
	 * @param page
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists/{categoryCd}")
	public ConcurrentHashMap<String, Object> getModelList(@PathVariable("categoryCd")
														  @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
														  @RequestParam(required = false) Map<String, Object> paramMap,
														  Page page) throws Exception {

		ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
		// 페이징 및 검색
		ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
		modelMap.put("categoryCd", categoryCd);

		Integer modelListCnt = this.adminModelApiService.getModelListCnt(modelMap);

		List<AdminModelDTO> modelList = new ArrayList<>();

		if (modelListCnt > 0) {
			modelList = this.adminModelApiService.getModelList(modelMap);
		}

		// 리스트 수
		resultMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		resultMap.put("perPageListCnt", Math.ceil((modelListCnt - 1) / page.getSize() + 1));
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
	 *
	 * @param categoryCd
	 * @param idx
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping("/{categoryCd}/{idx}")
	public ConcurrentHashMap<String, Object> getModelEdit(@PathVariable("categoryCd")
														  @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
														  @PathVariable("idx") Integer idx) throws Exception {
		ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Object> modelMap;

		AdminModelDTO adminModelDTO = builder().idx(idx).categoryCd(categoryCd).build();

		modelMap = this.adminModelApiService.getModelInfo(adminModelDTO);

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
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param newCommonDTO
	 * @param request
	 * @param files
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 등록", notes = "모델을 등록한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String insertModel(AdminModelDTO adminModelDTO,
							  CommonImageDTO commonImageDTO,
							  NewCommonDTO newCommonDTO,
							  HttpServletRequest request,
							  @RequestParam(name = "imageFiles", required = false) MultipartFile[] files) throws Exception {

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
	 *
	 * @param adminModelDTO
	 * @param commonImageDTO
	 * @param newCommonDTO
	 * @param request
	 * @param files
	 * @param idx
	 * @param categoryCd
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/{categoryCd}/{idx}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public String updateModel(@PathVariable(value = "idx") Integer idx,
							  @PathVariable(value = "categoryCd") Integer categoryCd,
							  @Valid AdminModelDTO adminModelDTO,
							  CommonImageDTO commonImageDTO,
							  NewCommonDTO newCommonDTO,
							  HttpServletRequest request,
							  @RequestParam(name = "imageFiles", required = false) MultipartFile[] files) throws Exception {

		searchCommon.giveAuth(request, newCommonDTO);

		Map<String, Object> modelMap = new ConcurrentHashMap<>();

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
	 *
	 * @param idx
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 이미지 삭제", notes = "모델 이미지를 삭제한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@DeleteMapping("/image/{idx}")
	public String deleteModelImage(@PathVariable(value = "idx") Integer idx) throws Exception {
		String result;

		CommonImageDTO commonImageDTO = CommonImageDTO.builder().idx(idx).build();

		if (this.adminModelApiService.deleteModelImage(commonImageDTO) > 0) {
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
	 *
	 * @param idx
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@DeleteMapping("/{idx}")
	public String deleteModel(@PathVariable(value = "idx") Integer idx) throws Exception {
		String result;

		AdminModelDTO adminModelDTO = AdminModelDTO.builder().visible("N").idx(idx).build();

		if (this.adminModelApiService.deleteModel(adminModelDTO) > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}
}
