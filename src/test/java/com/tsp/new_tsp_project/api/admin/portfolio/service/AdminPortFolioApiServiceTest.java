package com.tsp.new_tsp_project.api.admin.portfolio.service;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
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

import static com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
@DisplayName("프로덕션 Service Test")
class AdminPortFolioApiServiceTest {
    @Mock private final AdminPortFolioApiService mockAdminPortfolioApiService;
    private final AdminPortFolioApiService adminPortFolioApiService;

    @Test
    @DisplayName("포트폴리오 리스트 조회 테스트")
    void 포트폴리오리스트조회테스트() throws Exception {
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("startPage", 1);
        portfolioMap.put("size", 3);
        List<AdminPortFolioDTO> portfolioList = adminPortFolioApiService.getPortFolioList(portfolioMap);
        assertThat(portfolioList).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 Mockito 테스트")
    void 포트폴리오리스트조회Mockito테스트() throws Exception {
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("startPage", 1);
        portfolioMap.put("size", 3);
        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder().idx(1)
                .categoryCd(1).title("포트폴리오 테스트")
                .description("포트폴리오 테스트").videoUrl("https://youtube.com").hashTag("#test").visible("Y").build());

        // when
        when(mockAdminPortfolioApiService.getPortFolioList(portfolioMap)).thenReturn(returnPortfolioList);
        List<AdminPortFolioDTO> portfolioList = mockAdminPortfolioApiService.getPortFolioList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        verify(mockAdminPortfolioApiService, times(1)).getPortFolioList(portfolioMap);
        verify(mockAdminPortfolioApiService, atLeastOnce()).getPortFolioList(portfolioMap);
        verifyNoMoreInteractions(mockAdminPortfolioApiService);
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() throws Exception {
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("startPage", 1);
        portfolioMap.put("size", 3);
        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder().idx(1)
                .categoryCd(1).title("포트폴리오 테스트")
                .description("포트폴리오 테스트").videoUrl("https://youtube.com").hashTag("#test").visible("Y").build());

        // when
        given(mockAdminPortfolioApiService.getPortFolioList(portfolioMap)).willReturn(returnPortfolioList);
        List<AdminPortFolioDTO> portfolioList = mockAdminPortfolioApiService.getPortFolioList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        then(mockAdminPortfolioApiService).should(times(1)).getPortFolioList(portfolioMap);
        then(mockAdminPortfolioApiService).should(atLeastOnce()).getPortFolioList(portfolioMap);
        then(mockAdminPortfolioApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() throws Exception {
        assertThat(adminPortFolioApiService.getPortFolioInfo(builder().idx(1).build())).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 Mockito 테스트")
    void 포트폴리오상세조회Mockito테스트() throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder()
                .categoryCd(1).title("포트폴리오 테스트").description("포트폴리오 테스트")
                .videoUrl("https://youtube.com").hashTag("#test").visible("Y").build();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("portfolioInfo", mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO));

        // when
        when(mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO)).thenReturn(resultMap);

        // then
        assertThat(mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO).get("portfolioInfo")).isNotNull();

        // verify
        verify(mockAdminPortfolioApiService, times(2)).getPortFolioInfo(adminPortFolioDTO);
        verify(mockAdminPortfolioApiService, atLeastOnce()).getPortFolioInfo(adminPortFolioDTO);
        verifyNoMoreInteractions(mockAdminPortfolioApiService);
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() throws Exception {
        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder()
                .categoryCd(1).title("포트폴리오 테스트").description("포트폴리오 테스트")
                .videoUrl("https://youtube.com").hashTag("#test").visible("Y").build();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("portfolioInfo", mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO));

        // when
        given(mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO)).willReturn(resultMap);

        // then
        assertThat(mockAdminPortfolioApiService.getPortFolioInfo(adminPortFolioDTO).get("portfolioInfo")).isNotNull();

        // verify
        then(mockAdminPortfolioApiService).should(times(2)).getPortFolioInfo(adminPortFolioDTO);
        then(mockAdminPortfolioApiService).should(atLeastOnce()).getPortFolioInfo(adminPortFolioDTO);
        then(mockAdminPortfolioApiService).shouldHaveNoMoreInteractions();
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

        List<MultipartFile> imageFiles = asList(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        assertThat(adminPortFolioApiService.insertPortFolio(adminPortFolioDTO, commonImageDTO, imageFiles)).isPositive();
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() throws Exception {
        assertThat(adminPortFolioApiService.deletePortFolio(builder().idx(1).build())).isPositive();
    }
}