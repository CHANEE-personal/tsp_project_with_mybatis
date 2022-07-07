package com.tsp.new_tsp_project.api.admin.production.service;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminProductionApiServiceTest {
    @Autowired
    private AdminProductionApiService adminProductionApiService;

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
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() throws Exception {
        AdminProductionDTO adminProductionDTO = builder().idx(117).build();
        assertThat(adminProductionApiService.getProductionInfo(adminProductionDTO).get("productionInfo")).isNotNull();
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

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        Integer count = adminProductionApiService.insertProduction(adminProductionDTO, commonImageDTO, imageFiles);
        assertThat(count).isPositive();
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() throws Exception {
        AdminProductionDTO adminProductionDTO = builder().idx(117).build();
        Integer count = adminProductionApiService.deleteProduction(adminProductionDTO);
        assertThat(count).isPositive();
    }
}