package com.tsp.new_tsp_project.api.admin.support.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ApiModel
public class AdminSupportDTO {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	Integer rnum;

	@ApiModelProperty(value = "지원모델 IDX", required = true, hidden = true)
	private Integer idx;

	@ApiModelProperty(value = "지원모델 이름", required = true)
	private String supportName;

	@ApiModelProperty(value = "지원모델 Height", required = true)
	private Integer supportHeight;

	@ApiModelProperty(value = "지원모델 3Size", required = true)
	private String supportSize3;

	@ApiModelProperty(value = "지원모델 instagram")
	private String supportInstagram;

	@ApiModelProperty(value = "지원모델 휴대폰번호", required = true)
	private String supportPhone;

	@ApiModelProperty(value = "지원모델 내용", required = true)
	private String supportMessage;

	@ApiModelProperty(value = "노출여부", required = true)
	private String visible;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@ApiModelProperty(value = "지원 시각", required = true)
	private Date supportTime;

}
