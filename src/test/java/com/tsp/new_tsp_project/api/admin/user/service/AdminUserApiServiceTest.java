package com.tsp.new_tsp_project.api.admin.user.service;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO.builder;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("유저 Service Test")
class AdminUserApiServiceTest {
    @Mock
    private AdminUserApiService mockAdminUserApiService;
    private final AdminUserApiService adminUserApiService;

    @Test
    @DisplayName("관리자 회원 리스트 조회 테스트")
    void 관리자회원리스트조회테스트() throws Exception {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("startPage", 1);
        userMap.put("size", 3);
        List<AdminUserDTO> adminUserList = adminUserApiService.getUserList(userMap);

        // then
        assertThat(adminUserList).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 Mockito 테스트")
    void 관리자회원리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();

        returnUserList.add(AdminUserDTO.builder().idx(1).userId("admin05").password("test1234").name("admin05").visible("Y").build());

        // when
        when(mockAdminUserApiService.getUserList(userMap)).thenReturn(returnUserList);
        List<AdminUserDTO> userList = mockAdminUserApiService.getUserList(userMap);

        // then
        assertAll(
                () -> assertThat(userList).isNotEmpty(),
                () -> assertThat(userList).hasSize(1)
        );

        assertThat(userList.get(0).getIdx()).isEqualTo(returnUserList.get(0).getIdx());
        assertThat(userList.get(0).getUserId()).isEqualTo(returnUserList.get(0).getUserId());
        assertThat(userList.get(0).getPassword()).isEqualTo(returnUserList.get(0).getPassword());
        assertThat(userList.get(0).getName()).isEqualTo(returnUserList.get(0).getName());

        // verify
        verify(mockAdminUserApiService, times(1)).getUserList(userMap);
        verify(mockAdminUserApiService, atLeastOnce()).getUserList(userMap);
        verifyNoMoreInteractions(mockAdminUserApiService);

        InOrder inOrder = inOrder(mockAdminUserApiService);
        inOrder.verify(mockAdminUserApiService).getUserList(userMap);
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 BDD 테스트")
    void 관리자회원리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();

        returnUserList.add(AdminUserDTO.builder().idx(1).userId("admin05").password("test1234").name("admin05").visible("Y").build());

        // when
        given(mockAdminUserApiService.getUserList(userMap)).willReturn(returnUserList);
        List<AdminUserDTO> userList = mockAdminUserApiService.getUserList(userMap);

        // then
        assertAll(
                () -> assertThat(userList).isNotEmpty(),
                () -> assertThat(userList).hasSize(1)
        );

        assertThat(userList.get(0).getIdx()).isEqualTo(returnUserList.get(0).getIdx());
        assertThat(userList.get(0).getUserId()).isEqualTo(returnUserList.get(0).getUserId());
        assertThat(userList.get(0).getPassword()).isEqualTo(returnUserList.get(0).getPassword());
        assertThat(userList.get(0).getName()).isEqualTo(returnUserList.get(0).getName());

        // verify
        then(mockAdminUserApiService).should(times(1)).getUserList(userMap);
        then(mockAdminUserApiService).should(atLeastOnce()).getUserList(userMap);
        then(mockAdminUserApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("회원 로그인 테스트")
    void 회원로그인테스트() throws Exception {
        // given
        AdminUserDTO adminUserDTO = builder().userId("admin01").password("pass1234").build();
        // then
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
        // given
        AdminUserDTO adminUserDTO = builder()
                .userId("admin01")
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();
        adminUserApiService.insertUserToken(adminUserDTO);

        // then
        assertThat(adminUserApiService.findOneUserByToken(adminUserDTO.getUserToken())).isEqualTo("admin01");
    }
}