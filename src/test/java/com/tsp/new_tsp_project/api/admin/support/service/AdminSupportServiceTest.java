package com.tsp.new_tsp_project.api.admin.support.service;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("지원모델 Service Test")
class AdminSupportServiceTest {
    @Mock private final AdminSupportService mockAdminSupportService;
    private final AdminSupportService adminSupportService;

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
    @DisplayName("지원모델 리스트 조회 BDD 테스트")
    void 지원모델리스트조회BDD테스트() throws Exception {
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("startPage", 1);
        supportMap.put("size", 3);
        List<AdminSupportDTO> returnSupportList = new ArrayList<>();

        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1).supportName("조찬희").supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportHeight(170).supportSize3("31-24-31")
                .supportInstagram("https://instagram.com").build());

        when(mockAdminSupportService.getSupportModelList(supportMap)).thenReturn(returnSupportList);
        List<AdminSupportDTO> supportList = mockAdminSupportService.getSupportModelList(supportMap);

        // then
        assertAll(
                () -> assertThat(supportList).isNotEmpty(),
                () -> assertThat(supportList).hasSize(1)
        );

        assertThat(supportList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(supportList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(supportList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(supportList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(supportList.get(0).getSupportPhone()).isEqualTo(returnSupportList.get(0).getSupportPhone());
        assertThat(supportList.get(0).getSupportSize3()).isEqualTo(returnSupportList.get(0).getSupportSize3());

        // verify
        verify(mockAdminSupportService, times(1)).getSupportModelList(supportMap);
        verify(mockAdminSupportService, atLeastOnce()).getSupportModelList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportService);
    }

    @Test
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() throws Exception {
        assertThat(adminSupportService.getSupportModelInfo(builder().idx(1).build())).isNotEmpty();
    }
}