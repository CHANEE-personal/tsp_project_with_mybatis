package com.tsp.new_tsp_project.api.admin.production.service.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_project.api.admin.model.service.impl.jpa.ModelImageMapper;
import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.QAdminProductionEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.QCommonImageEntity;
import com.tsp.new_tsp_project.api.common.image.service.jpa.ImageRepository;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class ProductionRepository {

	private final QAdminProductionEntity qAdminProductionEntity = QAdminProductionEntity.adminProductionEntity;
	private final EntityManager em;
	private final ImageRepository imageRepository;


	private BooleanExpression searchProduction(Map<String, Object> productionMap) {
		String searchType = StringUtil.getString(productionMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(productionMap.get("searchKeyword"),"");

		if (productionMap == null) {
			return null;
		} else {
			if ("0".equals(searchType)) {
				return qAdminProductionEntity.title.contains(searchKeyword)
						.or(qAdminProductionEntity.description.contains(searchKeyword));
			} else if ("1".equals(searchType)) {
				return qAdminProductionEntity.title.contains(searchKeyword);
			} else {
				return qAdminProductionEntity.description.contains(searchKeyword);
			}
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findProductionCount
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param productionMap
	 * @throws Exception
	 */
	public Long findProductionCount(Map<String, Object> productionMap) throws Exception {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);

		return queryFactory.selectFrom(qAdminProductionEntity)
				.where(searchProduction(productionMap))
				.fetchCount();
	}

	/**
	 * <pre>
	 * 1. MethodName : findProductionList
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param productionMap
	 * @throws Exception
	 */
	public List<AdminProductionDTO> findProductionList(Map<String, Object> productionMap) throws Exception {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);

		List<AdminProductionEntity> productionList = queryFactory.selectFrom(qAdminProductionEntity)
				.where(searchProduction(productionMap))
				.orderBy(qAdminProductionEntity.idx.desc())
				.offset(StringUtil.getInt(productionMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(productionMap.get("size"),0))
				.fetch();

		List<AdminProductionDTO> productionDtoList = ProductionMapper.INSTANCE.toDtoList(productionList);

		for(int i = 0; i < productionDtoList.size(); i++) {
			productionDtoList.get(i).setRnum(StringUtil.getInt(productionMap.get("startPage"),1)*(StringUtil.getInt(productionMap.get("size"),1))-(2-i));
		}

		return productionDtoList;

	}

	/**
	 * <pre>
	 * 1. MethodName : findOneProduction
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @throws Exception
	 */
	public ConcurrentHashMap<String, Object> findOneProduction(AdminProductionEntity adminProductionEntity) throws Exception {
		QCommonImageEntity qCommonImageEntity = QCommonImageEntity.commonImageEntity;

		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

		//모델 상세 조회
		AdminProductionEntity findOneProduction = jpaQueryFactory.selectFrom(qAdminProductionEntity)
				.where(qAdminProductionEntity.idx.eq(adminProductionEntity.getIdx()))
				.fetchOne();

		//포트폴리오 이미지 조회
		List<CommonImageEntity> productionImageList = jpaQueryFactory.selectFrom(qCommonImageEntity)
				.where(qCommonImageEntity.typeIdx.eq(adminProductionEntity.getIdx()),
						qCommonImageEntity.visible.eq("Y"),
						qCommonImageEntity.typeName.eq("production")).fetch();

		ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();

		productionMap.put("productionInfo", ProductionMapper.INSTANCE.toDto(findOneProduction));
		productionMap.put("productionImageList", ModelImageMapper.INSTANCE.toDtoList(productionImageList));

		return productionMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertProduction
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @param commonImageEntity
	 * @param files
	 * @throws Exception
	 */
	public Integer insertProduction(AdminProductionEntity adminProductionEntity,
									CommonImageEntity commonImageEntity,
									MultipartFile[] files) throws Exception {
		Date date = new Date();
		adminProductionEntity.builder().createTime(date).creator(1).build();
		em.persist(adminProductionEntity);

		commonImageEntity.builder().typeName("production").typeIdx(adminProductionEntity.getIdx()).build();

		imageRepository.uploadImageFile(commonImageEntity, files);

		return adminProductionEntity.getIdx();
	}

	/**
	 * <pre>
	 * 1. MethodName : updateProduction
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 수정
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @param commonImageEntity
	 * @param files
	 * @throws Exception
	 */
	public Integer updateProduction(AdminProductionEntity adminProductionEntity,
									CommonImageEntity commonImageEntity,
									MultipartFile[] files) throws Exception {

		JPAUpdateClause update = new JPAUpdateClause(em, qAdminProductionEntity);

		Date currentTime = new Date();

		adminProductionEntity.builder().updateTime(currentTime).updater(1).build();

		update.set(qAdminProductionEntity.title, adminProductionEntity.getTitle())
				.set(qAdminProductionEntity.description, adminProductionEntity.getDescription())
				.set(qAdminProductionEntity.visible, "Y")
				.set(qAdminProductionEntity.updateTime, currentTime)
				.set(qAdminProductionEntity.updater, 1)
				.where(qAdminProductionEntity.idx.eq(adminProductionEntity.getIdx())).execute();

		commonImageEntity.setTypeName("production");
		commonImageEntity.setTypeIdx(adminProductionEntity.getIdx());

		ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
		productionMap.put("typeName", "portfolio");

		imageRepository.uploadImageFile(commonImageEntity, files);

		return 1;
	}

	/**
	 * <pre>
	 * 1. MethodName : deleteProduction
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 프로덕션 삭제
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 22.
	 * </pre>
	 *
	 * @param adminProductionEntity
	 * @throws Exception
	 */
	public Integer deleteProduction(AdminProductionEntity adminProductionEntity) throws Exception {
		JPAUpdateClause update = new JPAUpdateClause(em, qAdminProductionEntity);

		Date currentTime = new Date();

		adminProductionEntity.builder().updateTime(currentTime).updater(1).build();

		update.set(qAdminProductionEntity.title, adminProductionEntity.getTitle())
				.set(qAdminProductionEntity.visible, "N")
				.set(qAdminProductionEntity.updateTime, currentTime)
				.set(qAdminProductionEntity.updater, 1)
				.where(qAdminProductionEntity.idx.eq(adminProductionEntity.getIdx())).execute();

		return 1;
	}
}
