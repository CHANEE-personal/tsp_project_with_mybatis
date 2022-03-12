package com.tsp.new_tsp_project.api.admin.production.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminProductionJpaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("프로덕션리스트조회")
    public void 프로덕션리스트조회() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("프로덕션상세조회")
    public void 프로덕션상세조회() throws Exception {
        // 사용
        mockMvc.perform(get("/api/production/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionInfo.idx").value("1"))
                .andExpect(jsonPath("$.productionInfo.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.productionInfo.description").value("프로덕션 테스트"));

        // 미사용
        mockMvc.perform(get("/api/production/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PRODUCTION"));
    }

}