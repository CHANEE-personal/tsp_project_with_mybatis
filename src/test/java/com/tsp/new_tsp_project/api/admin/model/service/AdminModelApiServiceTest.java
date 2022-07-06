package com.tsp.new_tsp_project.api.admin.model.service;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
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

import static com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminModelApiServiceTest {

    @Autowired
    private AdminModelApiService adminModelApiService;

    @Test
    @DisplayName("관리자 모델 리스트 조회 테스트")
    void 관리자모델리스트조회테스트() throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("startPage", 1);
        modelMap.put("size", 3);
        List<AdminModelDTO> modelList = adminModelApiService.getModelList(modelMap);
        assertThat(modelList).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 모델 상세 조회 테스트")
    void 관리자모델상세조회테스트() throws Exception {
        AdminModelDTO adminModelDTO = builder().categoryCd(1).idx(156).build();
        adminModelApiService.getModelInfo(adminModelDTO);

        assertThat(adminModelApiService.getModelInfo(adminModelDTO).get("modelImageList")).isNotNull();
        assertThat(adminModelApiService.getModelInfo(adminModelDTO).get("modelInfo")).isNotNull();
    }

    @Test
    @DisplayName("관리자 모델 등록 테스트")
    void 관리자모델등록테스트() throws Exception {
        AdminModelDTO adminModelDTO = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

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

        Integer count = adminModelApiService.insertModel(adminModelDTO, commonImageDTO, imageFiles);
        assertThat(count).isPositive();
    }

    @Test
    @DisplayName("관리자 모델 삭제 테스트")
    void 관리자모델삭제테스트() throws Exception {
        AdminModelDTO adminModelDTO = AdminModelDTO.builder().idx(156).build();
        Integer count = adminModelApiService.deleteModel(adminModelDTO);
        assertThat(count).isPositive();
    }
}