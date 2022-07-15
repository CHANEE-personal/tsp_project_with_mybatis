package com.tsp.new_tsp_project.api.common;

import com.tsp.new_tsp_project.api.admin.mapper.AdminUserMapper;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
import com.tsp.new_tsp_project.common.paging.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_project.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_project.common.utils.StringUtil.getString;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchCommon {

    private final AdminUserMapper adminUserMapper;

    /**
     * <pre>
     * 1. MethodName : searchCommon
     * 2. ClassName  : SearchCommon.java
     * 3. Comment    : 페이징 및 검색 조건 공통
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 08. 08.
     * </pre>
     */
    public Map<String, Object> searchCommon(Page page, Map<String, Object> paramMap) {

        Map<String, Object> searchMap = new HashMap<>();

        // 페이징 처리
        Integer pageCnt = getInt(page.getPage(), 1);
        Integer pageSize = getInt(page.getSize(), 10);
        page.setPage(pageCnt);
        page.setSize(pageSize);

        // 검색 조건
        searchMap.put("searchType", getString(paramMap.get("searchType"), ""));
        searchMap.put("searchKeyword", getString(paramMap.get("searchKeyword"), ""));
        searchMap.put("jpaStartPage", getInt(page.getStartPage(), 0));
        searchMap.put("startPage", pageCnt);
        searchMap.put("size", pageSize);

        return searchMap;
    }

    /**
     * <pre>
     * 1. MethodName : giveAuth
     * 2. ClassName  : SearchCommon.java
     * 3. Comment    : jwt 인증자
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 08. 08.
     * </pre>
     */
    public void giveAuth(HttpServletRequest request, NewCommonDTO newCommonDTO) throws Exception {
        // creator, updater 공통 DTO

        // JWT token 값 존재 시 유저 인증 값 부여
        if (request.getHeader("Authorization") != null) {
            String userSeq = adminUserMapper.selectAdminSeq(getString(request.getHeader("Authorization"), ""));
            newCommonDTO.setCreator(userSeq);
            newCommonDTO.setUpdater(userSeq);
        }
    }
}
