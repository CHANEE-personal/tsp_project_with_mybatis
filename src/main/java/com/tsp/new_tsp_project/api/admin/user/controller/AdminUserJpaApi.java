package com.tsp.new_tsp_project.api.admin.user.controller;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.admin.user.entity.AdminUserEntity;
import com.tsp.new_tsp_project.api.admin.user.service.jpa.AdminUserJpaService;
import com.tsp.new_tsp_project.api.common.SearchCommon;
import com.tsp.new_tsp_project.api.jwt.AuthenticationRequest;
import com.tsp.new_tsp_project.api.jwt.AuthenticationResponse;
import com.tsp.new_tsp_project.api.jwt.JwtUtil;
import com.tsp.new_tsp_project.api.jwt.MyUserDetailsService;
import com.tsp.new_tsp_project.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.user.entity.AdminUserEntity.*;

@Slf4j
@RequestMapping(value = "/api/jpa-auth")
@RestController
@RequiredArgsConstructor
@Api(tags = "회원관련 API")
public class AdminUserJpaApi {
	private final AuthenticationManager authenticationManager;
	private final MyUserDetailsService userDetailsService;
	private final JwtUtil jwtTokenUtil;
	private final AdminUserJpaService adminUserJpaService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : findUserList
	 * 2. ClassName  : AdminUserJpaApi.java
	 * 3. Comment    : 관리자 유저 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param page
	 * @throws Exception
	 */
	@ApiOperation(value = "회원 조회", notes = "회원을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/users")
	public List<AdminUserDTO> findUserList(@RequestParam(required = false) Map<String, Object> paramMap,
										   Page page) throws Exception {
		// 페이징 및 검색
		ConcurrentHashMap userMap = searchCommon.searchCommon(page, paramMap);

		List<AdminUserDTO> userInfoList = this.adminUserJpaService.findUserList(userMap);

		return userInfoList;
	}

	/**
	 * <pre>
	 * 1. MethodName : admin-login
	 * 2. ClassName  : AdminUserJpaApi.java
	 * 3. Comment    : 관리자 로그인 처리
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 * @param  authenticationRequest
	 * @param  request
	 * @throws Exception
	 */
	@ApiOperation(value = "회원 로그인 처리", notes = "회원 로그인을 처리한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping(value = "/admin-login")
	public ConcurrentHashMap adminLogin(@RequestBody AuthenticationRequest authenticationRequest,
										HttpServletRequest request,
										HttpServletResponse response,
										BindingResult bindingResult) throws Exception {

		AdminUserEntity adminUserEntity = builder()
						.userId(authenticationRequest.getUserId())
						.password(authenticationRequest.getPassword())
						.build();

		String resultValue = adminUserJpaService.adminLogin(adminUserEntity, request, bindingResult);

		ConcurrentHashMap<String, Object> userMap = new ConcurrentHashMap<>();

		if ("Y".equals(resultValue)) {
			userMap.put("loginYn", resultValue);
			userMap.put("userId", adminUserEntity.getUserId());
			userMap.put("token", createAuthenticationToken(authenticationRequest));

			// 로그인 완료 시 생성된 token 값 DB에 저장
			UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
			String token = jwtTokenUtil.generateToken(userDetails);

			adminUserEntity.setUserToken(token);
			adminUserJpaService.insertUserToken(adminUserEntity);
		}

		return userMap;
	}

	/**
	 * <pre>
	 * 1. MethodName : insertAdminUser
	 * 2. ClassName  : AdminUserJpaApi.java
	 * 3. Comment    : 관리자 회원가입
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 * @param  adminUserEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "관리자 회원가입 처리", notes = "관리자 회원가입을 처리한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@PostMapping
	public String insertAdminUser(AdminUserEntity adminUserEntity) throws Exception {
		String result = "N";

		if(this.adminUserJpaService.insertAdminUser(adminUserEntity) > 0) {
			result = "Y";
		} else {
			result = "N";
		}

		return result;
	}

	/**
	 * <pre>
	 * 1. MethodName : createAuthenticationToken
	 * 2. ClassName  : AdminLoginApi.java
	 * 3. Comment    : 관리자 로그인 시 JWT 토큰 발급
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 * @param  authenticationRequest
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = "JWT 토근 발급", notes = "JWT 토근 발급")
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		// id, password 인증
		authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

		// 사용자 정보 조회 후 token 생성
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
		String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	/**
	 * <pre>
	 * 1. MethodName : authenticate
	 * 2. ClassName  : AdminLoginApi.java
	 * 3. Comment    : 관리자 로그인 시 인증
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 * @param  id
	 * @param  password
	 * @throws Exception
	 */
	private void authenticate(String id, String password) throws Exception {
		try {
			Authentication request = new UsernamePasswordAuthenticationToken(id, password);
			if (request.getName().equals(request.getCredentials())) {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getCredentials()));
			}
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
