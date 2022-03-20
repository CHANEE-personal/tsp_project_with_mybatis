package com.tsp.new_tsp_project.api.admin.model.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Mock
    private ModelRepository mockModelRepository;

    @Test
    public void 모델리스트조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // when
        List<AdminModelDTO> modelList = modelRepository.findModelsList(modelMap);

        // then
        assertThat(modelList.size()).isGreaterThan(0);
    }

    @Test
    public void 모델상세조회테스트() throws Exception {

        // given
        AdminModelEntity adminModelEntity = builder().idx(3).categoryCd(1).build();

        // when
        AdminModelDTO modelInfo = modelRepository.findOneModel(adminModelEntity);

        assertAll(() -> assertThat(modelInfo.getIdx()).isEqualTo(3),
                () -> {
                    assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
                    assertNotNull(modelInfo.getCategoryCd());
                },
                () -> {
                    assertThat(modelInfo.getCategoryAge()).isEqualTo("2");
                    assertNotNull(modelInfo.getCategoryAge());
                },
                () -> {
                    assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
                    assertNotNull(modelInfo.getModelKorName());
                },
                () -> {
                    assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
                    assertNotNull(modelInfo.getModelEngName());
                },
                () -> {
                    assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
                    assertNotNull(modelInfo.getModelDescription());
                },
                () -> {
                    assertThat(modelInfo.getHeight()).isEqualTo("170");
                    assertNotNull(modelInfo.getHeight());
                },
                () -> {
                    assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
                    assertNotNull(modelInfo.getSize3());
                },
                () -> {
                    assertThat(modelInfo.getShoes()).isEqualTo("270");
                    assertNotNull(modelInfo.getShoes());
                },
                () -> {
                    assertThat(modelInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(modelInfo.getVisible());
                });

        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959779.jpg");

        assertThat(modelInfo.getModelImage().get(1).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(modelInfo.getModelImage().get(1).getFileName()).isEqualTo("e13f6930-17a5-407c-96ed-fd625b720d21.jpg");
        assertThat(modelInfo.getModelImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959823.jpg");
    }

    @Test
    public void 모델BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        given(mockModelRepository.findModelsList(modelMap)).willReturn(modelList);

        // when
        Integer idx = mockModelRepository.findModelsList(modelMap).get(0).getIdx();
        Integer categoryCd = mockModelRepository.findModelsList(modelMap).get(0).getCategoryCd();
        String modelKorName = mockModelRepository.findModelsList(modelMap).get(0).getModelKorName();

        assertThat(idx).isEqualTo(modelList.get(0).getIdx());
        assertThat(categoryCd).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(modelKorName).isEqualTo(modelList.get(0).getModelKorName());
    }

    @Test
    public void 모델상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminModelEntity adminModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        AdminModelDTO adminModelDTO = AdminModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge("2")
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockModelRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);

        // when
        Integer idx = mockModelRepository.findOneModel(adminModelEntity).getIdx();
        Integer categoryCd = mockModelRepository.findOneModel(adminModelEntity).getCategoryCd();
        String categoryAge = mockModelRepository.findOneModel(adminModelEntity).getCategoryAge();
        String modelKorName = mockModelRepository.findOneModel(adminModelEntity).getModelKorName();
        String modelEngName = mockModelRepository.findOneModel(adminModelEntity).getModelEngName();
        String modelDescription = mockModelRepository.findOneModel(adminModelEntity).getModelDescription();
        String height = mockModelRepository.findOneModel(adminModelEntity).getHeight();
        String size3 = mockModelRepository.findOneModel(adminModelEntity).getSize3();
        String shoes = mockModelRepository.findOneModel(adminModelEntity).getShoes();
        String visible = mockModelRepository.findOneModel(adminModelEntity).getVisible();
        String fileName = mockModelRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileName();
        String fileMask = mockModelRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileMask();
        String filePath = mockModelRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFilePath();
        String imageType = mockModelRepository.findOneModel(adminModelEntity).getModelImage().get(0).getImageType();
        String typeName = mockModelRepository.findOneModel(adminModelEntity).getModelImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(categoryCd).isEqualTo(1);
        assertThat(categoryAge).isEqualTo("2");
        assertThat(modelKorName).isEqualTo("조찬희");
        assertThat(modelEngName).isEqualTo("CHOCHANHEE");
        assertThat(modelDescription).isEqualTo("chaneeCho");
        assertThat(height).isEqualTo("170");
        assertThat(size3).isEqualTo("34-24-34");
        assertThat(shoes).isEqualTo("270");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("model");
    }
}