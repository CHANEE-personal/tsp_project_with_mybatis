package com.tsp.new_tsp_project.api.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class NewCommonDTO {

	@ApiModelProperty(required = true, value = "등록자", hidden = true)
	private String creator;

	@ApiModelProperty(required = true, value = "수정자", hidden = true)
	private String updater;

	@ApiModelProperty(required = true, value = "등록자 이름", hidden = true)
	private String adminName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@ApiModelProperty(required = true, value = "등록 일자", hidden = true)
	private Date createTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@ApiModelProperty(required = true, value = "수정 일자", hidden = true)
	private Date updateTime;
}
