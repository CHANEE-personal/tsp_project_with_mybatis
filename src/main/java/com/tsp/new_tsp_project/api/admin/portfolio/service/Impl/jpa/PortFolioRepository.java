package com.tsp.new_tsp_project.api.admin.portfolio.service.Impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_project.api.admin.model.service.impl.jpa.ModelImageMapper;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonCodeEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.api.common.image.service.jpa.ImageRepository;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.QAdminPortFolioEntity.adminPortFolioEntity;
import static com.tsp.new_tsp_project.api.common.domain.entity.QCommonCodeEntity.commonCodeEntity;
import static com.tsp.new_tsp_project.api.common.domain.entity.QCommonImageEntity.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PortFolioRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;
	private final ImageRepository imageRepository;

	private BooleanExpression searchPortFolio(Map<String, Object> modelMap) {
		String searchType = StringUtil.getString(modelMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(modelMap.get("searchKeyword"),"");

		if ("0".equals(searchType)) {
			return adminPortFolioEntity.title.contains(searchKeyword)
					.or(adminPortFolioEntity.description.contains(searchKeyword));
		} else if ("1".equals(searchType)) {
			return adminPortFolioEntity.title.contains(searchKeyword);
		} else {
			return adminPortFolioEntity.description.contains(searchKeyword);
		}
	}


	/**
	 * <pre>
	 * 1. MethodName : findPortFolioCount
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	public Long findPortFolioCount(Map<String, Object> portFolioMap) {

		try {
			return queryFactory.selectFrom(adminPortFolioEntity)
					.where(searchPortFolio(portFolioMap))
					.fetchCount();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PORTFOLIO_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findPortFolioList
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	public List<AdminPortFolioDTO> findPortFolioList(Map<String, Object> portFolioMap) {

		try {
			List<AdminPortFolioEntity> portFolioList = queryFactory
					.selectFrom(adminPortFolioEntity)
					.where(searchPortFolio(portFolioMap))
					.orderBy(adminPortFolioEntity.idx.desc())
					.offset(StringUtil.getInt(portFolioMap.get("jpaStartPage"),0))
					.limit(StringUtil.getInt(portFolioMap.get("size"),0))
					.fetch();

			List<AdminPortFolioDTO> portFolioDtoList = PortFolioMapper.INSTANCE.toDtoList(portFolioList);

			for(int i = 0; i < portFolioDtoList.size(); i++) {
				portFolioDtoList.get(i).setRnum(StringUtil.getInt(portFolioMap.get("startPage"),1)*(StringUtil.getInt(portFolioMap.get("size"),1))-(2-i));
			}

			return portFolioDtoList;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PORTFOLIO_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : modelCommonCode
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 공통 코드 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param existPortFolioEntity
	 */
	public ConcurrentHashMap<String, Object> portFolioCommonCode(CommonCodeEntity existPortFolioEntity) {
		try {
			ConcurrentHashMap<String, Object> modelCommonMap = new ConcurrentHashMap<>();

			List<CommonCodeEntity> codeEntityList = queryFactory
					.selectFrom(commonCodeEntity)
					.where(commonCodeEntity.cmmType.eq(existPortFolioEntity.getCmmType()))
					.fetch();

			modelCommonMap.put("codeEntityList", codeEntityList);

			return modelCommonMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_COMMON);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findOnePortFolio
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param existAdminPortFolioEntity
	 */
	public ConcurrentHashMap<String, Object> findOnePortFolio(AdminPortFolioEntity existAdminPortFolioEntity) {

		try {
			//모델 상세 조회
			AdminPortFolioEntity findPortFolio = queryFactory.selectFrom(adminPortFolioEntity)
					.where(adminPortFolioEntity.idx.eq(existAdminPortFolioEntity.getIdx()))
					.fetchOne();

			//포트폴리오 이미지 조회
			List<CommonImageEntity> portFolioImageList = queryFactory
					.selectFrom(commonImageEntity)
					.where(commonImageEntity.typeIdx.eq(existAdminPortFolioEntity.getIdx()),
							commonImageEntity.visible.eq("Y"),
							commonImageEntity.typeName.eq("portfolio")).fetch();

			ConcurrentHashMap<String, Object> portFolioMap = new ConcurrentHashMap<>();

			portFolioMap.put("portFolioInfo", PortFolioMapper.INSTANCE.toDto(findPortFolio));
			portFolioMap.put("portFolioImageList", ModelImageMapper.INSTANCE.toDtoList(portFolioImageList));

			return portFolioMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PORTFOLIO);
		}

	}

	/**
	 * <pre>
	 * 1. MethodName : insertPortFolio
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param existAdminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 */
	public Integer insertPortFolio(AdminPortFolioEntity existAdminPortFolioEntity,
								   CommonImageEntity commonImageEntity,
								   MultipartFile[] files) {

		try {
			existAdminPortFolioEntity.builder().createTime(new Date()).creator(1).build();
			em.persist(adminPortFolioEntity);
			em.flush();
			em.clear();

			commonImageEntity.builder().typeName("portfolio").typeIdx(existAdminPortFolioEntity.getIdx()).build();

			imageRepository.uploadImageFile(commonImageEntity, files);

			return existAdminPortFolioEntity.getIdx();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : updatePortFolio
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param existAdminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@Modifying
	@Transactional
	public Integer updatePortFolio(AdminPortFolioEntity existAdminPortFolioEntity, CommonImageEntity commonImageEntity,
								   MultipartFile[] files, ConcurrentHashMap<String, Object> portFolioMap) {

		try {
			JPAUpdateClause update = new JPAUpdateClause(em, adminPortFolioEntity);

			existAdminPortFolioEntity.builder().updateTime(new Date()).updater(1).build();

			update.set(adminPortFolioEntity.title, existAdminPortFolioEntity.getTitle())
					.set(adminPortFolioEntity.description, existAdminPortFolioEntity.getDescription())
					.set(adminPortFolioEntity.hashTag, existAdminPortFolioEntity.getHashTag())
					.set(adminPortFolioEntity.videoUrl, existAdminPortFolioEntity.getVideoUrl())
					.set(adminPortFolioEntity.visible, "Y")
					.set(adminPortFolioEntity.updateTime, new Date())
					.set(adminPortFolioEntity.updater, 1)
					.where(adminPortFolioEntity.idx.eq(existAdminPortFolioEntity.getIdx())).execute();

			commonImageEntity.builder()
					.typeName("portfolio")
					.typeIdx(existAdminPortFolioEntity.getIdx())
					.build();

			portFolioMap.put("typeName", "portfolio");
			if("Y".equals(imageRepository.updateMultipleFile(commonImageEntity, files, portFolioMap))) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deletePortFolio
	 * 2. ClassName  : PortFolioRepository.java
	 * 3. Comment    : 관리자 포트폴리오 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 28.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	@Modifying
	@Transactional
	public Long deletePortFolio(Map<String, Object> portFolioMap) {

		try {
			JPAUpdateClause update = new JPAUpdateClause(em, adminPortFolioEntity);

			Long[] deleteIdx = (Long[]) portFolioMap.get("deleteIdx");

			return update.set(adminPortFolioEntity.visible, "N")
					.set(adminPortFolioEntity.updateTime, new Date())
					.set(adminPortFolioEntity.updater, 1)
					.where(adminPortFolioEntity.idx.in(deleteIdx)).execute();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_DELETE_PORTFOLIO);
		}
	}
}
