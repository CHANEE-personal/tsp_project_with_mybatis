package com.tsp.new_tsp_project.api.admin.production.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.model.service.impl.jpa.ModelImageMapper;
import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;
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

import static com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class ProductionRepositoryTest {

    @Autowired
    private ProductionRepository productionRepository;

    @Mock
    private ProductionRepository mockProductionReposity;

    @Test
    public void 프로덕션조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        // when
        List<AdminProductionDTO> productionList = productionRepository.findProductionList(productionMap);

        // then
        assertThat(productionList.size()).isGreaterThan(0);
    }

    @Test
    public void 프로덕션상세조회테스트() throws Exception {

        // given
        AdminProductionEntity adminProductionEntity = builder().idx(1).build();

        // when
        AdminProductionDTO productionInfo = productionRepository.findOneProduction(adminProductionEntity);

        assertAll(() -> assertThat(productionInfo.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
                    assertNotNull(productionInfo.getTitle());
                },
                () -> {
                    assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
                    assertNotNull(productionInfo.getDescription());
                },
                () -> {
                    assertThat(productionInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(productionInfo.getVisible());
                });

        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("1223024921206.jpg");
    }

    @Test
    public void 프로덕션BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());

        given(mockProductionReposity.findProductionList(productionMap)).willReturn(productionList);

        // when
        Integer idx = mockProductionReposity.findProductionList(productionMap).get(0).getIdx();
        String title = mockProductionReposity.findProductionList(productionMap).get(0).getTitle();
        String description = mockProductionReposity.findProductionList(productionMap).get(0).getDescription();
        String visible = mockProductionReposity.findProductionList(productionMap).get(0).getVisible();

        assertThat(idx).isEqualTo(productionList.get(0).getIdx());
        assertThat(title).isEqualTo(productionList.get(0).getTitle());
        assertThat(description).isEqualTo(productionList.get(0).getDescription());
        assertThat(visible).isEqualTo(productionList.get(0).getVisible());
    }

    @Test
    public void 프로덕션상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockProductionReposity.findOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);

        // when
        Integer idx = mockProductionReposity.findOneProduction(adminProductionEntity).getIdx();
        String title = mockProductionReposity.findOneProduction(adminProductionEntity).getTitle();
        String description = mockProductionReposity.findOneProduction(adminProductionEntity).getDescription();
        String visible = mockProductionReposity.findOneProduction(adminProductionEntity).getVisible();
        String fileName = mockProductionReposity.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFileName();
        String fileMask = mockProductionReposity.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFileMask();
        String filePath = mockProductionReposity.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFilePath();
        String imageType = mockProductionReposity.findOneProduction(adminProductionEntity).getProductionImage().get(0).getImageType();
        String typeName = mockProductionReposity.findOneProduction(adminProductionEntity).getProductionImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("프로덕션 테스트");
        assertThat(description).isEqualTo("프로덕션 테스트");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("production");
    }
}