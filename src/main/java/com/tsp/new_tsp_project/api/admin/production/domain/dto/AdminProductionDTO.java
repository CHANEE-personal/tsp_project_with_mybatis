package com.tsp.new_tsp_project.api.admin.production.domain.dto;

import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AdminProductionDTO extends NewCommonDTO {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	Integer rnum;

	@ApiModelProperty(required = true, value = "idx", hidden = true)
	Integer idx;

	@NotNull(message = "제목 입력은 필수입니다.")
	@ApiModelProperty(required = true, value = "title")
	String title;

	@NotNull(message = "상세 내용 입력은 필수입니다.")
	@ApiModelProperty(required = true, value = "description")
	String description;

	@ApiModelProperty(required = true, value = "visible")
	String visible;

	@ApiModelProperty(required = true, value = "productionImageList", hidden = true)
	private List<CommonImageDTO> productionImage = new ArrayList<>();
}
