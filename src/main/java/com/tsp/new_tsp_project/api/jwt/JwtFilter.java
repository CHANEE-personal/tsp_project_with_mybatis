package com.tsp.new_tsp_project.api.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.lang.Boolean.TRUE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService service;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        // Request Header에서 토큰 값 가져온다
        String authorizationHeader = jwtUtil.resolveToken((HttpServletRequest) servletRequest);
        String token = null;
        String userName = null;

        if (authorizationHeader != null) {
            token = authorizationHeader;
            userName = jwtUtil.extractUserName(token);
        }

        if (userName != null && getContext().getAuthentication() == null) {
            UserDetails userDetails = service.loadUserByUsername(userName);

            // 유효한 토큰인지 검증
            if (TRUE.equals(jwtUtil.validateToken(token, userDetails))) {
                // 토큰이 유효할 시 고객 정보 받아온다
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) servletRequest));

                // 받아온 고객 정보 SecurityContext에 저장
                getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
