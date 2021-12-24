package com.tsp.new_tsp_project.api.admin.support.service.Impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import com.tsp.new_tsp_project.api.admin.support.domain.entity.AdminSupportEntity;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.support.domain.entity.QAdminSupportEntity.*;

@Repository
@RequiredArgsConstructor
public class SupportRepository {

	private final JPAQueryFactory queryFactory;

	private BooleanExpression searchSupport(Map<String, Object> supportMap) {
		String searchType = StringUtil.getString(supportMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(supportMap.get("searchKeyword"),"");

		if ("0".equals(searchType)) {
			return adminSupportEntity.supportName.contains(searchKeyword)
					.or(adminSupportEntity.supportMessage.contains(searchKeyword));
		} else if ("1".equals(searchType)) {
			return adminSupportEntity.supportName.contains(searchKeyword);
		} else {
			return adminSupportEntity.supportMessage.contains(searchKeyword);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findSupportModelCount
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param supportMap
	 * @throws Exception
	 */
	public Long findSupportModelCount(Map<String, Object> supportMap) throws Exception {

		return queryFactory.selectFrom(adminSupportEntity)
				.where(searchSupport(supportMap))
				.fetchCount();
	}

	/**
	 * <pre>
	 * 1. MethodName : findSupportModelList
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 지원모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param supportMap
	 * @throws Exception
	 */
	public List<AdminSupportDTO> findSupportModelList(Map<String, Object> supportMap) throws Exception {

		List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
				.where(searchSupport(supportMap))
				.orderBy(adminSupportEntity.idx.desc())
				.offset(StringUtil.getInt(supportMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(supportMap.get("size"),0))
				.fetch();

		List<AdminSupportDTO> supportDtoList = SupportMapper.INSTANCE.toDtoList(supportList);

		for(int i = 0; i < supportDtoList.size(); i++) {
			supportDtoList.get(i).setRnum(StringUtil.getInt(supportMap.get("startPage"),1)*(StringUtil.getInt(supportMap.get("size"),1))-(2-i));
		}

		return supportDtoList;
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneSupportModel
	 * 2. ClassName  : ProductionRepository.java
	 * 3. Comment    : 관리자 지원모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 26.
	 * </pre>
	 *
	 * @param existAdminSupportEntity
	 * @throws Exception
	 */
	public Map<String, Object> findOneSupportModel(AdminSupportEntity existAdminSupportEntity) throws Exception {

		//모델 상세 조회
		AdminSupportEntity findOneSupportModel = queryFactory.selectFrom(adminSupportEntity)
				.where(adminSupportEntity.idx.eq(existAdminSupportEntity.getIdx()))
				.fetchOne();

		Map<String, Object> supportMap = new HashMap<>();

		supportMap.put("supportModelInfo", SupportMapper.INSTANCE.toDto(findOneSupportModel));

		return supportMap;
	}
}
