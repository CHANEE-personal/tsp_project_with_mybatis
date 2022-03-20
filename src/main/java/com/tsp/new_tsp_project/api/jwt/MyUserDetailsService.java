package com.tsp.new_tsp_project.api.jwt;

import com.tsp.new_tsp_project.api.admin.mapper.AdminUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminUserMapper adminUserMapper;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		SecurityUser securityUser = null;
		try {
			securityUser = adminUserMapper.getUserId(id);

			// 아이디 일치하는지 확인
			return new User(securityUser.getUsername(),
					securityUser.getPassword(),
					AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return securityUser;
	}
}
