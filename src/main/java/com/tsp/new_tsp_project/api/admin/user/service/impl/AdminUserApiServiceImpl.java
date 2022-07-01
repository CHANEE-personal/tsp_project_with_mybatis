package com.tsp.new_tsp_project.api.admin.user.service.impl;

import com.tsp.new_tsp_project.api.admin.mapper.AdminUserMapper;
import com.tsp.new_tsp_project.api.admin.user.service.AdminUserApiService;
import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.common.utils.StringUtils;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service("AdminUserApiService")
@Transactional
@RequiredArgsConstructor
public class AdminUserApiServiceImpl implements AdminUserApiService {

	private final AdminUserMapper adminUserMapper;
	private final PasswordEncoder passwordEncoder;

	/**
	 * <pre>
	 * 1. MethodName : getUserList
	 * 2. ClassName  : AdminUserApiServiceImpl.java
	 * 3. Comment    : 회원 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	@Override
	public List<AdminUserDTO> getUserList(Map<String, Object> commandMap) {
		return adminUserMapper.getUserList(commandMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : adminLogin
	 * 2. ClassName  : AdminUserApiServiceImpl.java
	 * 3. Comment    : 회원 로그인 처리
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	public String adminLogin(AdminUserDTO adminUserDTO, HttpServletRequest request) {

		try {
			final String db_pw = StringUtils.nullStrToStr(this.adminUserMapper.adminLogin(adminUserDTO));

			String result;

			if (passwordEncoder.matches(adminUserDTO.getPassword(), db_pw)) {
				result = "Y";
			} else {
				result = "N";
			}
			return result;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_USER, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : insertUserToken
	 * 2. ClassName  : AdminUserApiServiceImpl.java
	 * 3. Comment    : 회원 로그인 후 토큰 등록
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	public Integer insertUserToken(AdminUserDTO adminUserDTO) {
		try {
			return this.adminUserMapper.insertUserToken(adminUserDTO);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_USER, e);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : findOneUserByToken
	 * 2. ClassName  : AdminUserApiServiceImpl.java
	 * 3. Comment    : 토큰을 이용한 회원 아이디 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	public String findOneUserByToken(String token) {
		try {
			return this.adminUserMapper.findOneUserByToken(token);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_USER, e);
		}
	}
}
