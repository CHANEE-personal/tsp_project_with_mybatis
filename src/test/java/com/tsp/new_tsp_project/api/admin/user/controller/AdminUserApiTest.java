package com.tsp.new_tsp_project.api.admin.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.admin.user.service.AdminUserApiService;
import com.tsp.new_tsp_project.api.jwt.AuthenticationRequest;
import com.tsp.new_tsp_project.api.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO.builder;
import static com.tsp.new_tsp_project.api.admin.user.dto.Role.ROLE_ADMIN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class AdminUserApiTest {
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final JwtUtil jwtUtil;
    private final AdminUserApiService adminUserApiService;

    protected PasswordEncoder passwordEncoder;
    private AdminUserDTO adminUserDTO;
    private MockMvc mockMvc;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    void createUser() throws Exception {
        passwordEncoder = createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());

        adminUserDTO = builder()
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10))
                .visible("Y")
                .build();

        adminUserApiService.insertAdminUser(adminUserDTO);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        createUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 관리자 조회 테스트")
    void 관리자조회Api테스트() throws Exception {
        MultiValueMap<String, String> userMap = new LinkedMultiValueMap<>();
        userMap.add("jpaStartPage", "1");
        userMap.add("size", "3");
        mockMvc.perform(get("/api/auth/users").params(userMap)
                        .header("Authorization", adminUserDTO.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin 로그인 처리 테스트")
    void 관리자로그인Api테스트() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .userId("admin01")
                .password("pass1234")
                .build();

        mockMvc.perform(post("/api/auth/admin-login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginYn").value("Y"))
                .andExpect(jsonPath("$.userId").value("admin01"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Admin JWT 토큰 발급 테스트")
    void JWT토큰발급Api테스트() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .userId("admin01")
                .password("pass1234")
                .build();

        mockMvc.perform(post("/api/auth/authenticate")
                        .header("Authorization", adminUserDTO.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}