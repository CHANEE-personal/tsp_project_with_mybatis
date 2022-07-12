package com.tsp.new_tsp_project.api.admin.production.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.user.dto.AdminUserDTO;
import com.tsp.new_tsp_project.api.admin.user.dto.Role;
import com.tsp.new_tsp_project.api.admin.user.service.AdminUserApiService;
import com.tsp.new_tsp_project.api.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminProductionApiTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AdminUserApiService adminUserApiService;

	AdminUserDTO adminUserDTO;

	Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		return authorities;
	}

	void createUser() throws Exception {
		passwordEncoder = createDelegatingPasswordEncoder();

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
		String token = jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10);

		adminUserDTO = AdminUserDTO.builder()
				.userId("admin04")
				.password("pass1234")
				.name("test")
				.email("test@test.com")
				.role(Role.ROLE_ADMIN)
				.userToken(token)
				.visible("Y")
				.build();

		adminUserApiService.insertAdminUser(adminUserDTO);
	}

	@BeforeEach
	@EventListener(ApplicationReadyEvent.class)
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(wac)
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.apply(springSecurity())
				.alwaysDo(print())
				.build();

		createUser();
	}

	@Test
	@DisplayName("Admin 프로덕션 조회 테스트")
	void 프로덕션조회Api테스트() throws Exception {
		MultiValueMap<String, String> productionMap = new LinkedMultiValueMap<>();
		productionMap.add("jpaStartPage", "1");
		productionMap.add("size", "3");
		mockMvc.perform(get("/api/production/lists").params(productionMap)
						.header("Authorization", adminUserDTO.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Admin 프로덕션 상세 조회 테스트")
	void 프로덕션상세조회Api테스트() throws Exception {
		mockMvc.perform(get("/api/production/117")
						.header("Authorization", adminUserDTO.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.productionInfo.title").value("test"))
				.andExpect(jsonPath("$.productionInfo.description").value("test"));
	}

	@Test
	@DisplayName("Admin 프로덕션 등록 테스트")
	void 프로덕션등록Api테스트() throws Exception {
		AdminProductionDTO adminProductionDTO = builder()
				.title("productionTest")
				.description("productionTest")
				.visible("Y")
				.build();

		MockMultipartFile productionJson = new MockMultipartFile("adminProductionDTO", "",
				"application/json", objectMapper.writeValueAsString(adminProductionDTO).getBytes());

		List<MultipartFile> imageFiles = List.of(
				new MockMultipartFile("0522045010647","0522045010647.png",
						"image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
				new MockMultipartFile("0522045010772","0522045010772.png" ,
						"image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
		);

		mockMvc.perform(multipart("/api/production")
						.file("imageFiles", imageFiles.get(0).getBytes())
						.file("imageFiles", imageFiles.get(1).getBytes())
						.file(productionJson)
						.contentType(MULTIPART_FORM_DATA_VALUE)
						.contentType(APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Y"));
	}

	@Disabled
	@DisplayName("Admin 프로덕션 수정 테스트")
	void 프로덕션수정Api테스트() throws Exception {

	}

	@Test
	@DisplayName("Admin 프로덕션 삭제 테스트")
	void 프로덕션삭제Api테스트() throws Exception {
		mockMvc.perform(delete("/api/production/117")
						.header("Authorization", adminUserDTO.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Y"));
	}
}