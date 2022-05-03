package com.tsp.new_tsp_project.api.admin.model.controller;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import com.tsp.new_tsp_project.api.admin.model.service.jpa.AdminModelJpaService;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
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
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "모델관련 API")
@RequestMapping(value = "/api/jpa-model")
public class AdminModelJpaApi {

	private final AdminModelJpaService adminModelJpaService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : getModelList
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param categoryCd
	 * @param paramMap
	 * @param page
	 */
	@ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists/{categoryCd}")
	public ConcurrentHashMap<String, Object> getModelList(@PathVariable("categoryCd")
														  @Range(min = 1, max = 3, message = "{modelCategory.Range}")
														  Integer categoryCd, Map<String, Object> paramMap, Page page) {

		// 페이징 및 검색
		ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
		modelMap.put("categoryCd", categoryCd);

		Long modelListCnt = this.adminModelJpaService.findModelsCount(modelMap);

		List<AdminModelDTO> modelList = new ArrayList<>();

		if (modelListCnt > 0) {
			modelList = this.adminModelJpaService.findModelsList(modelMap);
		}

		// 리스트 수
		modelMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		modelMap.put("perPageListCnt", Math.ceil((modelListCnt - 1) / page.getSize() + 1));
		// 전체 아이템 수
		modelMap.put("modelListTotalCnt", modelListCnt);

		modelMap.put("modelList", modelList);

		return modelMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelEdit
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 상세
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param categoryCd
	 * @param idx
	 */
	@ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping("/{categoryCd}/{idx}")
	public AdminModelDTO getModelEdit(@PathVariable("categoryCd")
														  @Range(min = 1, max = 3, message = "{modelCategory.Range}")
														  Integer categoryCd,
														  @PathVariable("idx") Integer idx) {

		AdminModelEntity adminModelEntity = builder().idx(idx).categoryCd(categoryCd).build();

		return this.adminModelJpaService.findOneModel(adminModelEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : modelCommonCode
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 공통 코드 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	@ApiOperation(value = "모델 공통 코드 조회", notes = "모델을 공통 코드를 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/common")
	public ConcurrentHashMap<String, Object> modelCommonCode() {
		ConcurrentHashMap<String, Object> modelCmmCode = new ConcurrentHashMap<>();

		CommonCodeEntity modelCodeEntity = CommonCodeEntity.builder().cmmType("model").build();

		modelCmmCode.put("modelCmmCode", this.adminModelJpaService.modelCommonCode(modelCodeEntity));

		return modelCmmCode;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 등록", notes = "모델을 등록한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping
	public String insertModel(@RequestBody AdminModelEntity adminModelEntity) throws Exception {

		String result;

		if (this.adminModelJpaService.insertModel(adminModelEntity) > 0) {
			result = "Y";
		} else {
			result = "N";
		}

		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : updateModel
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 * @param commonImageEntity
	 * @param newCommonDTO
	 * @param request
	 * @param files
	 * @param categoryCd
	 * @param idx
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/{categoryCd}/{idx}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Integer updateModel(@PathVariable(value = "idx") Integer idx,
							   @PathVariable(value = "categoryCd")
							   @Range(min = 1, max = 3, message = "{modelCategory.Range}")
							   Integer categoryCd,
							   AdminModelEntity adminModelEntity,
							   CommonImageEntity commonImageEntity,
							   NewCommonDTO newCommonDTO,
							   HttpServletRequest request,
							   @RequestParam(name = "imageFiles", required = false) MultipartFile[] files) throws Exception {

		searchCommon.giveAuth(request, newCommonDTO);

		ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();

		String[] arrayState = request.getParameter("imageState").split(",");
		String[] arrayIdx = request.getParameter("idxState").split(",");

		modelMap.put("arrayState", arrayState);
		modelMap.put("arrayIdx", arrayIdx);

		builder().idx(idx).categoryCd(categoryCd).build();

		return this.adminModelJpaService.updateModel(adminModelEntity, commonImageEntity, files, modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteModel
	 * 2. ClassName  : AdminModelJpaApi.java
	 * 3. Comment    : 관리자 모델 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param idx
	 */
	@ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@DeleteMapping(value = "/{idx}")
	public String deleteModel(@PathVariable("idx") Integer idx) {
		String result;

		AdminModelEntity adminModelEntity = builder().visible("N").idx(idx).build();

		if (this.adminModelJpaService.deleteModel(adminModelEntity) > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}

}
