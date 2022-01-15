package com.tsp.new_tsp_project.api.admin.production.controller;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;
import com.tsp.new_tsp_project.api.admin.production.service.jpa.AdminProductionJpaService;
import com.tsp.new_tsp_project.api.common.SearchCommon;
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

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/jpa-production")
@Api(tags = "프로덕션 관련 API")
public class AdminProductionJpaApi {

	private final SearchCommon searchCommon;
	private final AdminProductionJpaService adminProductionJpaService;

	/**
	 * <pre>
	 * 1. MethodName : getProductionList
	 * 2. ClassName  : AdminProductionJpaApi.java
	 * 3. Comment    : 관리자 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param page
	 */
	@ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists")
	public ConcurrentHashMap getProductionList(Page page, @RequestParam(required = false) Map<String, Object> paramMap) {
		ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();

		ConcurrentHashMap<String, Object> searchMap = searchCommon.searchCommon(page, paramMap);

		Long productionCnt = this.adminProductionJpaService.findProductionCount(searchMap);

		List<AdminProductionDTO> productionList = new ArrayList<>();

		if (productionCnt > 0) {
			productionList = this.adminProductionJpaService.findProductionList(searchMap);
		}

		// 리스트 수
		productionMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		productionMap.put("perPageListCnt", Math.ceil((productionCnt - 1) / page.getSize() + 1));
		// 전체 아이템 수
		productionMap.put("productionListCnt", productionCnt);

		productionMap.put("productionList", productionList);

		return productionMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductInfo
	 * 2. ClassName  : AdminProductionJpaApi.java
	 * 3. Comment    : 관리자 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param idx
	 */
	@ApiOperation(value = "프로덕션 상세 조회", notes = "프로덕션을 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/{idx}")
	public ConcurrentHashMap getProductionInfo(@PathVariable("idx") Integer idx) {
		ConcurrentHashMap<String, Object> productionMap;

		AdminProductionEntity adminProductionEntity = builder().idx(idx).build();

		productionMap = this.adminProductionJpaService.findOneProduction(adminProductionEntity);

		return productionMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertProduction
	 * 2. ClassName  : AdminProductionJpaApi.java
	 * 3. Comment    : 관리자 프로덕션 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param
	 */
	@ApiOperation(value = "프로덕션 등록", notes = "프로덕션을 등록한다")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String insertProduction(@Valid AdminProductionEntity adminProductionEntity,
								   CommonImageEntity commonImageEntity,
								   @RequestParam(name = "imageFiles", required = false) MultipartFile[] files) {

		String result;

		if (this.adminProductionJpaService.insertProduction(adminProductionEntity, commonImageEntity, files) > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : updateProduction
	 * 2. ClassName  : AdminProductionJpaApi.java
	 * 3. Comment    : 관리자 프로덕션 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@ApiOperation(value = "프로덕션 수정", notes = "프로덕션을 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/{idx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String updateProduction(@PathVariable("idx") Integer idx,
								   @Valid AdminProductionEntity adminProductionEntity,
								   CommonImageEntity commonImageEntity,
								   @RequestParam(value = "imageFiles", required = false) MultipartFile[] files) {
		String result;

		builder().idx(idx).build();

		if (this.adminProductionJpaService.updateProduction(adminProductionEntity, commonImageEntity, files) > 0) {
			result = "Y";
		} else {
			result = "N";
		}

		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteProduction
	 * 2. ClassName  : AdminProductionJpaApi.java
	 * 3. Comment    : 관리자 프로덕션 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 10. 22
	 * </pre>
	 *
	 * @param idx
	 */
	@ApiOperation(value = "프로덕션 삭제", notes = "프로덕션을 삭제한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@DeleteMapping(value = "/{idx}")
	public String deleteProduction(@PathVariable("idx") Integer idx) {
		String result;

		AdminProductionEntity adminProductionEntity = builder().visible("N").idx(idx).build();

		if (this.adminProductionJpaService.deleteProduction(adminProductionEntity) > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}
}
