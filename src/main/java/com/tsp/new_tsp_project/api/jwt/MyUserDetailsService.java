package com.tsp.new_tsp_project.api.jwt;

import com.tsp.new_tsp_project.api.admin.mapper.AdminUserMapper;
import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.exception.ApiExceptionType;
import com.tsp.new_tsp_project.exception.TspException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.tsp.new_tsp_project.exception.ApiExceptionType.*;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminUserMapper adminUserMapper;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		try {
			AdminUserDTO adminUserDTO = adminUserMapper.getUserId(id);
			adminUserDTO.setUserRefreshToken(adminUserDTO.getUserToken());
			adminUserMapper.insertUserToken(adminUserDTO);

			// 아이디 일치하는지 확인
			return new User(adminUserDTO.getUserId(),
					adminUserDTO.getPassword(),
					AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_USER, e);
		}
	}
}
