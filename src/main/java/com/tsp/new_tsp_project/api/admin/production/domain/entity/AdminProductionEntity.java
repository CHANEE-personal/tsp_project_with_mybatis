package com.tsp.new_tsp_project.api.admin.production.domain.entity;

import com.tsp.new_tsp_project.api.common.domain.entity.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_production")
public class AdminProductionEntity extends NewCommonMappedClass {

	@Transient
	private Integer rnum;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "title")
	@NotEmpty(message = "프로덕션 제목 입력은 필수입니다.")
	private String title;

	@Column(name = "description")
	@Lob
	@NotEmpty(message = "프로덕션 상세내용 입력은 필수입니다.")
	private String description;

	@Column(name = "visible")
	private String visible;

}
