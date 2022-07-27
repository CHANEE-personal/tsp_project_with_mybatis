package com.tsp.new_tsp_project.api.jwt;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class JwtUtilTest {

    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    private final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    private UserDetails userDetails;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
        authenticationRequest.setUserId("admin02");
        authenticationRequest.setPassword("pass1234");
        userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateTokenTest() {
        assertNotNull(jwtUtil.generateToken(userDetails));
    }

    @Test
    void extractAllClaimsTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(jwtUtil.extractAllClaims(token));
    }

    @Test
    @DisplayName("토큰 유효성 테스트")
    void validateTokenTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    @DisplayName("엑세스 토큰 헤더 설정 테스트")
    void setHeaderAccessTokenTest() {
        String token = jwtUtil.generateToken(userDetails);
        jwtUtil.setHeaderAccessToken(response, token);
        assertNotNull(response.getHeader("Authorization"));
        assertThat(token).isEqualTo(response.getHeader("Authorization"));
    }

//    @Test
//    @DisplayName("엑세스 리프레시 토큰 헤더 설정 테스트")
//    void setHeaderRefreshTokenTest() {
//        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
//        jwtUtil.setHeaderRefreshToken(response, refreshToken);
//        assertNotNull(response.getHeader("refreshToken"));
//        assertThat("Bearer " + refreshToken).isEqualTo(response.getHeader("refreshToken"));
//    }
}