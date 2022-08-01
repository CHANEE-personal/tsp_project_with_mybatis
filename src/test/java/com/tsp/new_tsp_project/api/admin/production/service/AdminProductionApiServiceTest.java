package com.tsp.new_tsp_project.api.admin.production.service;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class AdminProductionApiServiceTest {
    @Mock private final AdminProductionApiService mockAdminProductionApiService;
    private final AdminProductionApiService adminProductionApiService;

    @Test
    @DisplayName("프로덕션 리스트 조회 테스트")
    void 프로덕션리스트조회테스트() throws Exception {
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("startPage", 1);
        productionMap.put("size", 3);
        List<AdminProductionDTO> productionList = adminProductionApiService.getProductionList(productionMap);
        assertThat(productionList).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() throws Exception {
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("startPage", 1);
        productionMap.put("size", 3);
        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1).title("프로덕션 테스트").description("프로덕션 테스트").visible("Y").build());
        // when
        when(mockAdminProductionApiService.getProductionList(productionMap)).thenReturn(returnProductionList);
        List<AdminProductionDTO> productionList = mockAdminProductionApiService.getProductionList(productionMap);

        // then
        assertAll(
                () -> assertThat(productionList).isNotEmpty(),
                () -> assertThat(productionList).hasSize(1)
        );

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        verify(mockAdminProductionApiService, times(1)).getProductionList(productionMap);
        verify(mockAdminProductionApiService, atLeastOnce()).getProductionList(productionMap);
        verifyNoMoreInteractions(mockAdminProductionApiService);
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() throws Exception {
        assertThat(adminProductionApiService.getProductionInfo(builder().idx(117).build()).get("productionInfo")).isNotNull();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 BDD 테스트")
    void 프로덕션상세조회BDD테스트() throws Exception {
        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("productionInfo", mockAdminProductionApiService.getProductionInfo(adminProductionDTO));

        // when
        when(mockAdminProductionApiService.getProductionInfo(adminProductionDTO)).thenReturn(resultMap);

        // then
        assertThat(mockAdminProductionApiService.getProductionInfo(adminProductionDTO).get("productionInfo")).isNotNull();

        // verify
        verify(mockAdminProductionApiService, times(2)).getProductionInfo(adminProductionDTO);
        verify(mockAdminProductionApiService, atLeastOnce()).getProductionInfo(adminProductionDTO);
        verifyNoMoreInteractions(mockAdminProductionApiService);
    }

    @Test
    @DisplayName("프로덕션 등록 테스트")
    void 관리자프로덕션등록테스트() throws Exception {
        AdminProductionDTO adminProductionDTO = builder()
                .title("테스트").description("테스트").visible("Y").build();

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<MultipartFile> imageFiles = asList(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        assertThat(adminProductionApiService.insertProduction(adminProductionDTO, commonImageDTO, imageFiles)).isPositive();
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() throws Exception {
        assertThat(adminProductionApiService.deleteProduction(builder().idx(117).build())).isPositive();
    }
}