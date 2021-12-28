package com.tsp.new_tsp_project.api.admin.portfolio.domain.entity;

import com.tsp.new_tsp_project.api.common.domain.entity.NewCodeEntity;
import com.tsp.new_tsp_project.api.common.domain.entity.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_portfolio")
public class AdminPortFolioEntity extends NewCommonMappedClass {

	@Transient
	private Integer rnum;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "category_cd")
	@NotEmpty(message = "포트폴리오 카테고리 선택은 필수입니다.")
	private Integer categoryCd;

	@Column(name = "title")
	@NotEmpty(message = "포트폴리오 제목 입력은 필수입니다.")
	private String title;

	@Column(name = "description")
	@Lob
	@NotEmpty(message = "포트폴리오 상세내용 입력은 필수입니다.")
	private String description;

	@Column(name = "hash_tag")
	@NotEmpty(message = "포트폴리오 해시태그 입력은 필수입니다.")
	private String hashTag;

	@Column(name = "video_url")
	@NotEmpty(message = "포트폴리오 비디오URL 입력은 필수입니다.")
	private String videoUrl;

	@Column(name = "visible")
	private String visible;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "category_cd", insertable = false, updatable = false)
	private NewCodeEntity newPortFolioJpaDTO;
}
