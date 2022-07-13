package com.tsp.new_tsp_project.interceptor;

import com.tsp.new_tsp_project.api.jwt.JwtUtil;
import com.tsp.new_tsp_project.api.jwt.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
//@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;
	private final MyUserDetailsService userDetailsService;

	@Autowired
	public LoginCheckInterceptor(JwtUtil jwtUtil, MyUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// Request Header에서 토큰 값 가져온다
		String authorizationHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (authorizationHeader != null) {
			token = authorizationHeader;
			userName = jwtUtil.extractUserName(token);
		}

		if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
			log.debug("if request options method is options, return true");

			return true;
		}

		if (userName != null) {
			log.info("인증 사용자");
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

			// 유효한 토큰인지 검증
			if (jwtUtil.validateToken(token, userDetails)) {
				// 토큰이 유효할 시 고객 정보 받아온다
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// 받아온 고객 정보 SecurityContext에 저장
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			}

			return true;
		} else {
			log.info("미인증 사용자");
			response.sendRedirect("/api/auth/login");
			return false;
		}

	}
}
