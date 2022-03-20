package com.tsp.new_tsp_project.api.admin.portfolio.domain.dto;

import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class AdminPortFolioDTO extends NewCommonDTO {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	Integer rnum;

	@ApiModelProperty(required = true, value = "idx", hidden = true)
	Integer idx;

	@NotNull(message = "제목은 필수입니다.")
	@ApiModelProperty(required = true, value = "title")
	String title;

	@ApiModelProperty(required = true, value = "hashTag")
	String hashTag;

	@NotNull(message = "카테고리 선택은 필수입니다.")
	@ApiModelProperty(required = true, value = "categoryCd")
	Integer categoryCd;

	@ApiModelProperty(required = true, value = "videoUrl")
	String videoUrl;

	@NotNull(message = "상세 내용 입력은 필수입니다.")
	@ApiModelProperty(required = true, value = "description")
	String description;

	@ApiModelProperty(required = true, value = "visible")
	String visible;

	@ApiModelProperty(required = true, value = "portfolioImageList", hidden = true)
	private List<CommonImageDTO> portfolioImage = new ArrayList<>();
}
