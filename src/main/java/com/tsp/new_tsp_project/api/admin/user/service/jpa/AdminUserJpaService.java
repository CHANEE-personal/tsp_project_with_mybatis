package com.tsp.new_tsp_project.api.admin.user.service.jpa;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.admin.user.entity.AdminUserEntity;
import com.tsp.new_tsp_project.api.admin.user.service.impl.jpa.UserRepository;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import com.tsp.new_tsp_project.common.utils.StringUtils;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserJpaService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	/**
	 * <pre>
	 * 1. MethodName : findUserList
	 * 2. ClassName  : AdminUserJpaService.java
	 * 3. Comment    : 관리자 유저 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param userMap
	 */
	@Transactional(readOnly = true)
	public List<AdminUserDTO> findUserList(Map<String, Object> userMap) {
		return userRepository.findUserList(userMap);
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
	 * @param adminUserEntity
	 * @return result
	 */
	@Transactional(readOnly = true)
	public String adminLogin(@Validated AdminUserEntity adminUserEntity) {

		String password = StringUtil.getString(this.userRepository.adminLogin(adminUserEntity).get("password"),"");

		final String db_pw = StringUtils.nullStrToStr(password);

		String result;

		if (passwordEncoder.matches(adminUserEntity.getPassword(), db_pw)) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
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
	 * @param adminUserEntity
	 * @return result
	 */
	@Modifying
	@Transactional
	public Integer insertUserToken(AdminUserEntity adminUserEntity) {
		return this.userRepository.insertUserToken(adminUserEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : insertAdminUser
	 * 2. ClassName  : AdminUserApiServiceImpl.java
	 * 3. Comment    : 관리자 회원가입
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 * @param adminUserEntity
	 * @return result
	 */
	@Transactional
	public Integer insertAdminUser(AdminUserEntity adminUserEntity) {

		try {
			String userId = StringUtil.getString(this.userRepository.adminLogin(adminUserEntity).get("userId"),"");

			if(userId.equals(adminUserEntity.getUserId())) {
				throw new TspException(ApiExceptionType.ID_EXIST);
			}
			// 패스워드 인코딩
			String password = passwordEncoder.encode(adminUserEntity.getPassword());

			AdminUserEntity encodeUserEntity = AdminUserEntity.builder()
					.userId(adminUserEntity.getUserId())
					.password(password)
					.email(adminUserEntity.getEmail())
					.name(adminUserEntity.getName())
					.visible("Y")
					.creator(1)
					.createTime(new Date())
					.updater(1)
					.updateTime(new Date())
					.build();

			return this.userRepository.insertAdminUser(encodeUserEntity);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.ERROR_USER);
		}
	}
}
