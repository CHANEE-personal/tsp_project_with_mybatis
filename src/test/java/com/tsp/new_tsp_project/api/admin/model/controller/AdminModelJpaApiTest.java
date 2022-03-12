package com.tsp.new_tsp_project.api.admin.model.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminModelJpaApiTest {

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
    @DisplayName("남성모델리스트조회")
    public void 남성모델조회() throws Exception {
        mockMvc.perform(get("/api/jpa-model/lists/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("남성모델상세조회")
    public void 남성모델상세조회() throws Exception {
        mockMvc.perform(get("/api/jpa-model/1/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelMap.modelInfo.idx").value("3"))
                .andExpect(jsonPath("$.modelMap.modelInfo.categoryCd").value("1"))
                .andExpect(jsonPath("$.modelMap.modelInfo.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelMap.modelInfo.modelSecondName").value("CHAN HEE"))
                .andExpect(jsonPath("$.modelMap.modelInfo.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelMap.modelInfo.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelMap.modelInfo.height").value("170"))
                .andExpect(jsonPath("$.modelMap.modelInfo.size3").value("34-24-34"))
                .andExpect(jsonPath("$.modelMap.modelInfo.shoes").value("270"))
                .andExpect(jsonPath("$.modelMap.modelInfo.modelDescription").value("chaneeCho"));
    }

    @Test
    @DisplayName("남성모델등록")
    public void 남성모델등록() throws Exception {

        AdminModelEntity adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .modelKorName("조찬희")
                .modelEngName("chanhee")
                .modelDescription("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHAN HEE")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelMainYn("Y")
                .visible("Y")
                .height("170")
                .shoes("270")
                .size3("34-24-34")
                .categoryAge("2")
                .creator(1)
                .createTime(new Date())
                .build();

        final String jsonStr = objectMapper.writeValueAsString(adminModelEntity);

//        mockMvc.perform(post("/api/jpa-model")
////                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
////                        .content(jsonStr))
////                .andDo(print())
////                .andExpect(status().isOk());
    }

}