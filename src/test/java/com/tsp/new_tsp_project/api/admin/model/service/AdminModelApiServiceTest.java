package com.tsp.new_tsp_project.api.admin.model.service;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
@DisplayName("모델 Service Test")
class AdminModelApiServiceTest {
    @Mock private AdminModelApiService mockAdminModelApiService;
    private final AdminModelApiService adminModelApiService;


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
    @DisplayName("관리자 모델 리스트 조회 Mockito 테스트")
    void 모델리스트조회Mockito테스트() throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<AdminModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").build());

        // when
        when(mockAdminModelApiService.getModelList(modelMap)).thenReturn(returnModelList);
        List<AdminModelDTO> modelList = mockAdminModelApiService.getModelList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());

        // verify
        verify(mockAdminModelApiService, times(1)).getModelList(modelMap);
        verify(mockAdminModelApiService, atLeastOnce()).getModelList(modelMap);
        verifyNoMoreInteractions(mockAdminModelApiService);

        InOrder inOrder = inOrder(mockAdminModelApiService);
        inOrder.verify(mockAdminModelApiService).getModelList(modelMap);
    }

    @Test
    @DisplayName("관리자 모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<AdminModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").build());

        // when
        given(mockAdminModelApiService.getModelList(modelMap)).willReturn(returnModelList);
        List<AdminModelDTO> modelList = mockAdminModelApiService.getModelList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());

        // verify
        then(mockAdminModelApiService).should(times(1)).getModelList(modelMap);
        then(mockAdminModelApiService).should(atLeastOnce()).getModelList(modelMap);
        then(mockAdminModelApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 모델 상세 조회 테스트")
    void 관리자모델상세조회테스트() throws Exception {
        // given
        adminModelApiService.getModelInfo(AdminModelDTO.builder().categoryCd(1).idx(156).build());

        // then
        assertThat(adminModelApiService.getModelInfo(AdminModelDTO.builder().categoryCd(1).idx(156).build()).get("modelImageList")).isNotNull();
        assertThat(adminModelApiService.getModelInfo(AdminModelDTO.builder().categoryCd(1).idx(156).build()).get("modelInfo")).isNotNull();
    }

    @Test
    @DisplayName("관리자 모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() throws Exception {
        AdminModelDTO adminModelDTO = AdminModelDTO.builder()
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

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("modelInfo", mockAdminModelApiService.getModelInfo(adminModelDTO));

        // when
        when(mockAdminModelApiService.getModelInfo(adminModelDTO)).thenReturn(resultMap);

        // then
        assertThat(mockAdminModelApiService.getModelInfo(adminModelDTO).get("modelInfo")).isNotNull();

        // verify
        verify(mockAdminModelApiService, times(2)).getModelInfo(adminModelDTO);
        verify(mockAdminModelApiService, atLeastOnce()).getModelInfo(adminModelDTO);
        verifyNoMoreInteractions(mockAdminModelApiService);

        InOrder inOrder = inOrder(mockAdminModelApiService);
        inOrder.verify(mockAdminModelApiService).getModelInfo(adminModelDTO);
    }

    @Test
    @DisplayName("관리자 모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() throws Exception {
        AdminModelDTO adminModelDTO = AdminModelDTO.builder()
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

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("modelInfo", mockAdminModelApiService.getModelInfo(adminModelDTO));

        // when
        given(mockAdminModelApiService.getModelInfo(adminModelDTO)).willReturn(resultMap);

        // then
        assertThat(mockAdminModelApiService.getModelInfo(adminModelDTO).get("modelInfo")).isNotNull();

        // verify
        then(mockAdminModelApiService).should(times(2)).getModelInfo(adminModelDTO);
        then(mockAdminModelApiService).should(atLeastOnce()).getModelInfo(adminModelDTO);
        then(mockAdminModelApiService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 모델 등록 테스트")
    void 관리자모델등록테스트() throws Exception {
        AdminModelDTO adminModelDTO = AdminModelDTO.builder()
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

        List<MultipartFile> imageFiles = asList(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        // then
        assertThat(adminModelApiService.insertModel(adminModelDTO, commonImageDTO, imageFiles)).isPositive();
    }

    @Test
    @DisplayName("관리자 모델 삭제 테스트")
    void 관리자모델삭제테스트() throws Exception {
        assertThat(adminModelApiService.deleteModel(AdminModelDTO.builder().idx(156).build())).isPositive();
    }
}