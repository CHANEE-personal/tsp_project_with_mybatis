package com.tsp.new_tsp_project.api.admin.support.service;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminSupportServiceTest {
    @Autowired private AdminSupportService adminSupportService;

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() throws Exception {
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("startPage", 1);
        supportMap.put("size", 3);
        List<AdminSupportDTO> supportList = adminSupportService.getSupportModelList(supportMap);
        assertThat(supportList).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() throws Exception {
        assertThat(adminSupportService.getSupportModelInfo(builder().idx(1).build())).isNotEmpty();
    }
}