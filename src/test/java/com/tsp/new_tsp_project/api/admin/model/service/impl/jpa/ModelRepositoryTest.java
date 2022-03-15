package com.tsp.new_tsp_project.api.admin.model.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.model.domain.dto.AdminModelDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

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
}