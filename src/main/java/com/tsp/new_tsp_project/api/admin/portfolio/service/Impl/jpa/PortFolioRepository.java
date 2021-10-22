package com.tsp.new_tsp_project.api.admin.portfolio.service.Impl.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_project.api.admin.model.service.impl.jpa.ModelImageMapper;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.QAdminPortFolioEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.QCommonImageEntity;
import com.tsp.new_tsp_project.api.common.image.service.jpa.ImageRepository;
import com.tsp.new_tsp_project.common.utils.StringUtil;
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

@Slf4j
@RequiredArgsConstructor
@Repository
public class PortFolioRepository {

	private final EntityManager em;
	private final ImageRepository imageRepository;


	private String getPortFolioQuery(Map<String, Object> portFolioMap) {
		String query = "select m from AdminPortFolioEntity m join fetch m.newCodeJpaDTO where m.visible = :visible and m.newCodeJpaDTO.cmmType = :cmmType";

		if ("0".equals(StringUtil.getString(portFolioMap.get("searchType"), "0"))) {
			query += " and (m.title like :searchKeyword or m.description like :searchKeyword)";
		} else if ("1".equals(StringUtil.getString(portFolioMap.get("searchType"), "0"))) {
			query += " and m.title like :searchKeyword";
		} else {
			query += " and m.description like :searchKeyword";
		}
		return query;
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
	 * @throws Exception
	 */
	public Integer findPortFolioCount(Map<String, Object> portFolioMap) throws Exception {
		String query = getPortFolioQuery(portFolioMap);

		return em.createQuery(query, AdminPortFolioEntity.class)
				.setParameter("searchKeyword", "%" + StringUtil.getString(portFolioMap.get("searchKeyword"),"") + "%")
				.setParameter("visible", "Y")
				.setParameter("cmmType","portfolio")
				.getResultList().size();
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
	 * @throws Exception
	 */
	public List<AdminPortFolioDTO> findPortFolioList(Map<String, Object> portFolioMap) throws Exception {
		String query = getPortFolioQuery(portFolioMap);

		List<AdminPortFolioEntity> portFolioList = em.createQuery(query, AdminPortFolioEntity.class)
				.setParameter("searchKeyword", "%" + StringUtil.getString(portFolioMap.get("searchKeyword"),"") + "%")
				.setParameter("visible", "Y")
				.setParameter("cmmType", "portfolio")
				.setFirstResult(StringUtil.getInt(portFolioMap.get("jpaStartPage"),0))
				.setMaxResults(StringUtil.getInt(portFolioMap.get("size"),0))
				.getResultList();

		List<AdminPortFolioDTO> portFolioDtoList = PortFolioMapper.INSTANCE.toDtoList(portFolioList);

		for(int i = 0; i < portFolioDtoList.size(); i++) {
			portFolioDtoList.get(i).setRnum(StringUtil.getInt(portFolioMap.get("startPage"),1)*(StringUtil.getInt(portFolioMap.get("size"),1))-(2-i));
		}

		return portFolioDtoList;
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
	 * @param adminPortFolioEntity
	 * @throws Exception
	 */
	public ConcurrentHashMap<String, Object> findOnePortFolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception {
		QAdminPortFolioEntity qAdminPortFolioEntity = QAdminPortFolioEntity.adminPortFolioEntity;
		QCommonImageEntity qCommonImageEntity = QCommonImageEntity.commonImageEntity;

		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

		//모델 상세 조회
		AdminPortFolioEntity findPortFolio = jpaQueryFactory.selectFrom(qAdminPortFolioEntity)
				.where(qAdminPortFolioEntity.idx.eq(adminPortFolioEntity.getIdx()))
				.fetchOne();

		//포트폴리오 이미지 조회
		List<CommonImageEntity> portFolioImageList = jpaQueryFactory.selectFrom(qCommonImageEntity)
				.where(qCommonImageEntity.typeIdx.eq(adminPortFolioEntity.getIdx()),
						qCommonImageEntity.visible.eq("Y"),
						qCommonImageEntity.typeName.eq("portfolio")).fetch();

		ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();

		modelMap.put("portFolioInfo", PortFolioMapper.INSTANCE.toDto(findPortFolio));
		modelMap.put("portFolioImageList", ModelImageMapper.INSTANCE.toDtoList(portFolioImageList));

		return modelMap;

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
	 * @param adminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 * @throws Exception
	 */
	 public Integer insertPortFolio(AdminPortFolioEntity adminPortFolioEntity, CommonImageEntity commonImageEntity, MultipartFile[] files) throws Exception {
		 Date date = new Date();
		 adminPortFolioEntity.builder().createTime(date).creator(1).build();
		 em.persist(adminPortFolioEntity);

		 commonImageEntity.builder().typeName("portfolio").typeIdx(adminPortFolioEntity.getIdx()).build();

		 imageRepository.uploadImageFile(commonImageEntity, files);

		 return adminPortFolioEntity.getIdx();
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
	 * @param adminPortFolioEntity
	 * @param commonImageEntity
	 * @param files
	 * @throws Exception
	 */
	@Modifying
	@Transactional
	public Integer updatePortFolio(AdminPortFolioEntity adminPortFolioEntity, CommonImageEntity commonImageEntity,
							   MultipartFile[] files, ConcurrentHashMap<String, Object> portFolioMap) throws Exception {

		QAdminPortFolioEntity qAdminPortFolioEntity = QAdminPortFolioEntity.adminPortFolioEntity;

		JPAUpdateClause update = new JPAUpdateClause(em, qAdminPortFolioEntity);

		Date currentTime = new Date();

		adminPortFolioEntity.builder().updateTime(currentTime).updater(1).build();

		update.set(qAdminPortFolioEntity.title, adminPortFolioEntity.getTitle())
				.set(qAdminPortFolioEntity.description, adminPortFolioEntity.getDescription())
				.set(qAdminPortFolioEntity.hashTag, adminPortFolioEntity.getHashTag())
				.set(qAdminPortFolioEntity.videoUrl, adminPortFolioEntity.getVideoUrl())
				.set(qAdminPortFolioEntity.visible, "Y")
				.set(qAdminPortFolioEntity.updateTime, currentTime)
				.set(qAdminPortFolioEntity.updater, 1)
				.where(qAdminPortFolioEntity.idx.eq(adminPortFolioEntity.getIdx())).execute();

		commonImageEntity.setTypeName("portfolio");
		commonImageEntity.setTypeIdx(adminPortFolioEntity.getIdx());

		portFolioMap.put("typeName", "portfolio");
		imageRepository.updateMultipleFile(commonImageEntity, files, portFolioMap);

		return 1;
	}
}