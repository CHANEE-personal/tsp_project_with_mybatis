package com.tsp.new_tsp_project.api.admin.portfolio.service.Impl.jpa;

import com.tsp.new_tsp_project.api.admin.portfolio.domain.dto.AdminPortFolioDTO;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.domain.entity.CommonImageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class PortFolioRepositoryTest {

    @Autowired
    private PortFolioRepository portFolioRepository;

    @Mock
    private PortFolioRepository mockPortFolioRepository;

    @Test
    public void 포트포리오조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // when
        List<AdminPortFolioDTO> portfolioList = portFolioRepository.findPortFolioList(portfolioMap);

        // then
        assertThat(portfolioList.size()).isGreaterThan(0);
    }

    @Test
    public void 포트폴리오상세조회테스트() throws Exception {

        // given
        AdminPortFolioEntity adminPortFolioEntity = builder().idx(1).build();

        // when
        AdminPortFolioDTO portfolioInfo = portFolioRepository.findOnePortFolio(adminPortFolioEntity);

        assertAll(() -> assertThat(portfolioInfo.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(portfolioInfo.getTitle());
                },
                () -> {
                    assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
                    assertNotNull(portfolioInfo.getDescription());
                },
                () -> {
                    assertThat(portfolioInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(portfolioInfo.getVisible());
                },
                () -> {
                    assertThat(portfolioInfo.getHashTag()).isEqualTo("TEST");
                    assertNotNull(portfolioInfo.getHashTag());
                },
                () -> {
                    assertThat(portfolioInfo.getVideoUrl()).isEqualTo("http://youtube.com");
                    assertNotNull(portfolioInfo.getVideoUrl());
                });

        assertThat(portfolioInfo.getPortfolioImage().get(0).getTypeName()).isEqualTo("portfolio");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getImageType()).isEqualTo("main");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(portfolioInfo.getPortfolioImage().get(0).getFileMask()).isEqualTo("1223043918525.jpg");
    }

    @Test
    public void 포트폴리오BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1).title("포트폴리오 테스트")
                .description("포트폴리오 테스트").portfolioImage(commonImageDtoList).build());

        given(mockPortFolioRepository.findPortFolioList(portfolioMap)).willReturn(portfolioList);

        // when
        Integer idx = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getIdx();
        String title = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getTitle();
        String description = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getDescription();
        String visible = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getVisible();
        String hashTag = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getHashTag();
        String videoUrl = mockPortFolioRepository.findPortFolioList(portfolioMap).get(0).getVideoUrl();

        assertThat(idx).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(title).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(description).isEqualTo(portfolioList.get(0).getDescription());
        assertThat(visible).isEqualTo(portfolioList.get(0).getVisible());
        assertThat(hashTag).isEqualTo(portfolioList.get(0).getHashTag());
        assertThat(videoUrl).isEqualTo(portfolioList.get(0).getVideoUrl());
    }

    @Test
    public void 포트폴리오상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminPortFolioEntity adminPortFolioEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .visible("Y")
                .hashTag("TEST")
                .videoUrl("https://youtube.com")
                .portfolioImage(PortfolioImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);

        Integer idx = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getIdx();
        String title = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getTitle();
        String description = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getDescription();
        String visible = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getVisible();
        String fileName = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getPortfolioImage().get(0).getFileName();
        String fileMask = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getPortfolioImage().get(0).getFileMask();
        String filePath = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getPortfolioImage().get(0).getFilePath();
        String imageType = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getPortfolioImage().get(0).getImageType();
        String typeName = mockPortFolioRepository.findOnePortFolio(adminPortFolioEntity).getPortfolioImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("포트폴리오 테스트");
        assertThat(description).isEqualTo("포트폴리오 테스트");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("portfolio");
    }
}