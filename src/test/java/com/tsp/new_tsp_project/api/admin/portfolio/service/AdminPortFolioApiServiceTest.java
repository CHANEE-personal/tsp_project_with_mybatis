package com.tsp.new_tsp_project.api.admin.portfolio.service;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
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

import static com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminPortFolioApiServiceTest {

    @Autowired
    private AdminPortFolioApiService adminPortFolioApiService;

    @Test
    @DisplayName("포트폴리오 리스트 조회 테스트")
    void 포트폴리오리스트조회테스트() throws Exception {
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("startPate", 1);
        portfolioMap.put("size", 3);
        List<AdminPortFolioDTO> portfoliList = adminPortFolioApiService.getPortFolioList(portfolioMap);
        assertThat(portfoliList).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = builder().idx(1).build();
        assertThat(adminPortFolioApiService.getPortFolioInfo(adminPortFolioDTO)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 등록 테스트")
    void 포트폴리오등록테스트() throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = builder()
                .categoryCd(1).title("테스트").description("테스트").videoUrl("https://youtube.com")
                .visible("Y").hashTag("#test").build();

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

        Integer count = adminPortFolioApiService.insertPortFolio(adminPortFolioDTO, commonImageDTO, imageFiles);
        assertThat(count).isPositive();
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = builder().idx(1).build();
        Integer count = adminPortFolioApiService.deletePortFolio(adminPortFolioDTO);
        assertThat(count).isPositive();
    }
}