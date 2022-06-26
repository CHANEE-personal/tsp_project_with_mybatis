package com.tsp.new_tsp_project.api.admin.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminPortFolioApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext wac;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.apply(springSecurity())
				.alwaysDo(print())
				.build();
	}

	@Test
	@Disabled
	@DisplayName("Admin 포트폴리오 조회 테스트")
	public void 포트폴리오조회Api테스트() throws Exception {
		MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
		portfolioMap.put("jpaStartPage", Collections.singletonList("1"));
		portfolioMap.put("size", Collections.singletonList("3"));
		mockMvc.perform(get("/api/portfolio/lists").params(portfolioMap))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Disabled
	@DisplayName("Admin 포트폴리오 상세 조회 테스트")
	public void 포트폴리오상세조회Api테스트() throws Exception {
		mockMvc.perform(get("/api/portfolio/1"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Disabled
	@DisplayName("Admin 포트폴리오 등록 테스트")
	public void 포트폴리오등록Api테스트() throws Exception {
		AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder()
				.categoryCd(1)
				.title("test")
				.description("test")
				.videoUrl("https://youtube.com")
				.visible("Y").build();

		List<MultipartFile> imageFiles = List.of(
				new MockMultipartFile("0522045010647","0522045010647.png",
						"image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
				new MockMultipartFile("0522045010772","0522045010772.png" ,
						"image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
		);

		mockMvc.perform(multipart("/api/portfolio")
						.file("imageFiles", imageFiles.get(0).getBytes())
						.file("imageFiles", imageFiles.get(1).getBytes())
						.content(objectMapper.writeValueAsString(adminPortFolioDTO))
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Y"));
	}

	@Test
	@Disabled
	@DisplayName("Admin 포트폴리오 삭제 테스트")
	public void 포트폴리오삭제Api테스트() throws Exception {
		mockMvc.perform(delete("/api/portfolio/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("Y"));
	}
}