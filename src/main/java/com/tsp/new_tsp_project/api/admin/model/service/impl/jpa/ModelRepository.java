package com.tsp.new_tsp_project.api.admin.model.service.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
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
import javax.persistence.EntityTransaction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.model.domain.entity.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_project.api.common.domain.entity.QCommonCodeEntity.commonCodeEntity;
import static com.tsp.new_tsp_project.api.common.domain.entity.QCommonImageEntity.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ModelRepository {

	private final ImageRepository imageRepository;
	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	private BooleanExpression searchModel(Map<String, Object> modelMap) {
		String searchType = StringUtil.getString(modelMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(modelMap.get("searchKeyword"),"");
		Integer categoryCd = StringUtil.getInt(modelMap.get("categoryCd"),0);

		if ("0".equals(searchType)) {
			return adminModelEntity.modelKorName.contains(searchKeyword)
					.or(adminModelEntity.modelEngName.contains(searchKeyword)
							.or(adminModelEntity.modelDescription.contains(searchKeyword)))
					.and(adminModelEntity.categoryCd.eq(categoryCd));
		} else if ("1".equals(searchType)) {
			return adminModelEntity.modelKorName.contains(searchKeyword)
					.or(adminModelEntity.modelEngName.contains(searchKeyword))
					.and(adminModelEntity.categoryCd.eq(categoryCd));
		} else {
			return adminModelEntity.modelDescription.contains(searchKeyword).and(adminModelEntity.categoryCd.eq(categoryCd));
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findModelsCount
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 */
	public Long findModelsCount(Map<String, Object> modelMap) {

		try {
			return queryFactory.selectFrom(adminModelEntity)
					.where(searchModel(modelMap))
					.fetchCount();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
	}


	/**
	 * <pre>
	 * 1. MethodName : findModelsList
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param modelMap
	 */
	public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {

		try {
			List<AdminModelEntity> modelList = queryFactory
					.selectFrom(adminModelEntity)
					.orderBy(adminModelEntity.idx.desc())
					.where(searchModel(modelMap))
					.offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
					.limit(StringUtil.getInt(modelMap.get("size"),0))
					.fetch();

			for(int i = 0; i < modelList.size(); i++) {
				modelList.get(i).setRnum(StringUtil.getInt(modelMap.get("startPage"),1)*(StringUtil.getInt(modelMap.get("size"),1))-(2-i));
			}

			return ModelMapper.INSTANCE.toDtoList(modelList);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
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
	 * @param existModelCodeEntity
	 */
	public ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity existModelCodeEntity) {
		try {
			ConcurrentHashMap<String, Object> modelCommonMap = new ConcurrentHashMap<>();

			List<CommonCodeEntity> codeEntityList = queryFactory
					.selectFrom(commonCodeEntity)
					.where(commonCodeEntity.cmmType.eq(existModelCodeEntity.getCmmType()))
					.fetch();

			modelCommonMap.put("codeEntityList", codeEntityList);

			return modelCommonMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_COMMON);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneModel
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param existAdminModelEntity
	 */
	public AdminModelDTO findOneModel(AdminModelEntity existAdminModelEntity) {

		try {
			//모델 상세 조회
			AdminModelEntity findModel = queryFactory
					.selectFrom(adminModelEntity)
					.orderBy(adminModelEntity.idx.desc())
					.leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
					.fetchJoin()
					.where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())
							.and(adminModelEntity.visible.eq("Y"))
							.and(commonImageEntity.typeName.eq("model")))
					.fetchOne();

			return ModelMapper.INSTANCE.toDto(findModel);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : insertModel
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminModelEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@Transactional
	public Integer insertModel(AdminModelEntity adminModelEntity) {
		try {
			em.persist(adminModelEntity);

			return adminModelEntity.getIdx();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_MODEL);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : updateModel
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param existAdminModelEntity
	 * @param commonImageEntity
	 * @param files
	 */
	@Modifying
	@Transactional
	public Integer updateModel(AdminModelEntity existAdminModelEntity, CommonImageEntity commonImageEntity,
							   MultipartFile[] files, ConcurrentHashMap<String, Object> modelMap) {

		try {
			JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);

			existAdminModelEntity.builder().updateTime(new Date()).updater(1).build();

			update.set(adminModelEntity.modelKorName, existAdminModelEntity.getModelKorName())
					.set(adminModelEntity.categoryCd, existAdminModelEntity.getCategoryCd())
					.set(adminModelEntity.modelEngName, existAdminModelEntity.getModelEngName())
					.set(adminModelEntity.modelDescription, existAdminModelEntity.getModelDescription())
					.set(adminModelEntity.height, existAdminModelEntity.getHeight())
					.set(adminModelEntity.size3, existAdminModelEntity.getSize3())
					.set(adminModelEntity.shoes, existAdminModelEntity.getShoes())
					.set(adminModelEntity.categoryAge, existAdminModelEntity.getCategoryAge())
					.set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
					.set(adminModelEntity.updater, 1)
					.where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();

			commonImageEntity.builder()
					.typeName("model")
					.typeIdx(existAdminModelEntity.getIdx())
					.build();

			modelMap.put("typeName", "model");

			if("Y".equals(imageRepository.updateMultipleFile(commonImageEntity, files, modelMap))) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_MODEL);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteModel
	 * 2. ClassName  : ModelRepository.java
	 * 3. Comment    : 관리자 모델 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param existAdminModelEntity
	 */
	public Long deleteModel(AdminModelEntity existAdminModelEntity) {

		try {
			JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);

			existAdminModelEntity.builder().updateTime(new Date ()).updater(1).build();

			return update.set(adminModelEntity.visible, "N")
					.set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
					.set(adminModelEntity.updater, 1)
					.where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_DELETE_MODEL);
		}
	}
}
