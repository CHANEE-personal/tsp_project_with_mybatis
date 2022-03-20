package com.tsp.new_tsp_project.api.common.domain.entity;

import com.tsp.new_tsp_project.api.admin.model.domain.entity.AdminModelEntity;
import com.tsp.new_tsp_project.api.admin.portfolio.domain.entity.AdminPortFolioEntity;
import com.tsp.new_tsp_project.api.admin.production.domain.entity.AdminProductionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_image")
public class CommonImageEntity {

	@Id
	@GeneratedValue
	@Column(name = "idx")
	@ApiModelProperty(value = "파일 IDX", required = true, hidden = true)
	private Integer idx;

	@Column(name = "type_idx")
	@ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
	private Integer typeIdx;

	@Column(name = "type_name")
	@ApiModelProperty(value = "분야명", required = true, hidden = true)
	private String typeName;

	@Column(name = "file_num")
	@ApiModelProperty(value = "파일 Number", required = true, hidden = true)
	private Integer fileNum;

	@Column(name = "file_name")
	@ApiModelProperty(required = true, value = "파일명", hidden = true)
	private String fileName;

	@Column(name = "file_size")
	@ApiModelProperty(value = "파일SIZE", hidden = true)
	private Long fileSize;

	@Column(name = "file_mask")
	@ApiModelProperty(value = "파일MASK", hidden = true)
	private String fileMask;

	@Column(name = "file_path")
	@ApiModelProperty(value = "파일경로", hidden = true)
	private String filePath;

	@Column(name = "image_type")
	@ApiModelProperty(value = "메인 이미지 구분", hidden = true)
	private String imageType;

	@Column(name = "visible")
	@ApiModelProperty(value = "사용 여부", hidden = true)
	private String visible;

	@Column(name = "reg_date", insertable = false, updatable = false)
	@ApiModelProperty(value = "등록일자", hidden = true)
	private String regDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
	private AdminModelEntity adminModelEntity;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
	private AdminProductionEntity adminProductionEntity;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
	private AdminPortFolioEntity adminPortfolioEntity;
}
