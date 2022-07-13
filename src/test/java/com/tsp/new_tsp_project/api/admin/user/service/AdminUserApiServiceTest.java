package com.tsp.new_tsp_project.api.admin.user.service;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO.builder;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminUserApiServiceTest {
    @Autowired private AdminUserApiService adminUserApiService;

    @Test
    @DisplayName("관리자 회원 리스트 조회 테스트")
    void 관리자회원리스트조회테스트() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("startPage", 1);
        userMap.put("size", 3);
        List<AdminUserDTO> adminUserList = adminUserApiService.getUserList(userMap);
        assertThat(adminUserList).isNotEmpty();
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    void 회원로그인테스트() throws Exception {
        AdminUserDTO adminUserDTO = builder().userId("admin01").password("pass1234").build();
        assertThat(adminUserApiService.adminLogin(adminUserDTO, new MockHttpServletRequest())).isEqualTo("Y");
    }

    @Test
    @DisplayName("로그인 후 토큰 등록 테스트")
    void 로그인후토큰등록테스트() throws Exception {
        AdminUserDTO adminUserDTO = builder().userId("admin01").password("pass1234").build();
        adminUserApiService.insertUserToken(adminUserDTO);
    }

    @Test
    @DisplayName("토큰을 이용한 회원 조회 테스트")
    void 토큰을이용한회원조회테스트() throws Exception {
        AdminUserDTO adminUserDTO = builder()
                .userId("admin01")
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();
        adminUserApiService.insertUserToken(adminUserDTO);

        assertThat(adminUserApiService.findOneUserByToken(adminUserDTO.getUserToken())).isEqualTo("admin01");
    }
}