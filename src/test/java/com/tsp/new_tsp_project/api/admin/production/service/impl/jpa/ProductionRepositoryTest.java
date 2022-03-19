package com.tsp.new_tsp_project.api.admin.production.service.impl.jpa;

import com.tsp.new_tsp_project.api.admin.production.domain.dto.AdminProductionDTO;
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
@DisplayName("프로덕션 Repository Test")
class ProductionRepositoryTest {

    @Autowired
    private ProductionRepository productionRepository;

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
}