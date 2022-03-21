package com.tsp.new_tsp_project.api.admin.support.service.Impl.jpa;

import com.tsp.new_tsp_project.api.admin.support.domain.dto.AdminSupportDTO;
import com.tsp.new_tsp_project.api.admin.support.domain.entity.AdminSupportEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원하기 Repository Test")
class SupportRepositoryTest {

    @Autowired
    private SupportRepository supportRepository;

    @Mock
    private SupportRepository mockSupportRepository;

    @Test
    public void 지원하기조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> supportMap = new ConcurrentHashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // when
        List<AdminSupportDTO> supportList = supportRepository.findSupportModelList(supportMap);

        // then
        assertThat(supportList.size()).isGreaterThan(0);
    }

    @Test
    public void 지원하기상세조회테스트() throws Exception {

        // given
        AdminSupportEntity adminSupportEntity = AdminSupportEntity.builder().idx(1).build();

        // when
        AdminSupportDTO supportInfo = supportRepository.findOneSupportModel(adminSupportEntity);

        assertThat(supportInfo.getIdx()).isEqualTo(1);
    }
}