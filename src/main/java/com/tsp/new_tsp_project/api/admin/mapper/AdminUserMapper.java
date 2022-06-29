package com.tsp.new_tsp_project.api.admin.mapper;

import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.jwt.SecurityUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminUserMapper {

	/**
	 * <pre>
	 * 1. MethodName : getUserList
	 * 2. ClassName  : AdminUserMapper.java
	 * 3. Comment    : 관리자 유저 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	List<AdminUserDTO> getUserList(Map<String, Object> commandMap);

	/**
	 * <pre>
	 * 1. MethodName : getUserId
	 * 2. ClassName  : AdminUserMapper.java
	 * 3. Comment    : 관리자 유저 아이디 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	SecurityUser getUserId(String id) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : adminLogin
	 * 2. ClassName  : AdminUserMapper.java
	 * 3. Comment    : 관리자 로그인 처리
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	String adminLogin(AdminUserDTO adminUserDTO) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : selectAdminSeq
	 * 2. ClassName  : AdminUserMapper.java
	 * 3. Comment    : 관리자 로그인 처리 후 seq 값 부여
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 09. 08.
	 * </pre>
	 *
	 */
	String selectAdminSeq(String userToken) throws Exception;

	/**
	 * <pre>
	 * 1. MethodName : insertUserToken
	 * 2. ClassName  : AdminLoginApiMapper.java
	 * 3. Comment    : 관리자 로그인 처리 후 seq 값 부여
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 04. 23.
	 * </pre>
	 *
	 */
	Integer insertUserToken(AdminUserDTO adminUserDTO) throws Exception;
}
