package com.tsp.new_tsp_project.api.admin.user.service;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.exception.TspException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public interface AdminUserApiService {

	/**
	 * <pre>
	 * 1. MethodName : getUserList
	 * 2. ClassName  : AdminUserApiService.java
	 * 3. Comment    : 관리자 유저 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	List<AdminUserDTO> getUserList(Map<String, Object> commandMap) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertAdminUser
	 * 2. ClassName  : AdminUserApiService.java
	 * 3. Comment    : 관리자 회원 가입
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	Integer insertAdminUser(AdminUserDTO adminUserDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : adminLogin
	 * 2. ClassName  : AdminLoginApiService.java
	 * 3. Comment    : 회원 로그인 처리
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 */
	String adminLogin(AdminUserDTO adminUserDTO, HttpServletRequest request) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertUserToken
	 * 2. ClassName  : AdminUserApiService.java
	 * 3. Comment    : 회원 로그인 후 토큰 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 */
	void insertUserToken(AdminUserDTO adminUserDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : findOneUserByToken
	 * 2. ClassName  : AdminUserApiService.java
	 * 3. Comment    : 토큰을 이용한 회원 아이디 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	String findOneUserByToken(String token) throws TspException;
}
