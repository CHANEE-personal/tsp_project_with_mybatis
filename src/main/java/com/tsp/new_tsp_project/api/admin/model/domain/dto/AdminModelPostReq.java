package com.tsp.new_tsp_project.api.admin.model.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "모델 관련 변수")
public class AdminModelPostReq {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	private Integer rnum;

	@ApiModelProperty(required = true, value = "idx", hidden = true)
	private Integer idx;

	@ApiModelProperty(position = 1, required = true, value = "남자,여자,시니어 모델 구분((ex)1,2,3)")
	private Integer categoryCd;

	@ApiModelProperty(position = 2, required = true, value = "모델 연령대((ex)2:20,3:30***)")
	private String categoryAge;

	//	@NotNull(messages = "모델 국문 이름 입력은 필수입니다.")
	@ApiModelProperty(required = true, value = "men Kor Name", hidden = true)
	private String modelKorName;

	//	@NotNull(messages = "모델 영문 이름 입력은 필수입니다.")
	@ApiModelProperty(required = true, value = "men Eng Name", hidden = true)
	private String modelEngName;

	@NotNull(message = "모델 상세 내용 입력은 필수입니다.")
	@ApiModelProperty(position = 7, required = true, value = "모델 상세 설명")
	private String modelDescription;

	@ApiModelProperty(position = 8, required = true, value = "모델 키((ex)180)")
	@NotNull(message = "모델 키 입력은 필수입니다.")
//	@Pattern(regexp="\\\\d{1,3}", message = "숫자만 입력 가능합니다.")
//	@Length(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
	private String height;

	@NotNull(message = "모델 사이즈 입력은 필수입니다.")
//	@Pattern(regexp="/^([0-9]{2})$/-?([0-9]{2})$/-?([0-9]{2})$/", message = "**-**-** 형식으로 입력바랍니다.")
	@ApiModelProperty(position = 9, required = true, value = "모델 사이즈((ex)31-24-31")
	private String size3;

	@ApiModelProperty(position = 10, required = true, value = "모델 신발 사이즈((ex)270")
	@NotNull(message = "모델 신발 사이즈 입력은 필수입니다.")
//	@Pattern(regexp="[0-9]{3}", message = "숫자만 입력 가능합니다.")
//	@Length(min=1, max=4, message = "1자 이상 4자미만으로 작성해야 합니다.")
	private String shoes;

	@ApiModelProperty(position = 12, required = true, value = "모델 노출 여부((ex)Y,N")
	private String visible;

	@ApiModelProperty(position = 11, required = true, value = "모델 메인 전시 여부((ex)Y,N")
	private String modelMainYn;

	@NotNull(message = "모델 영문 성 입력은 필수입니다.")
	@ApiModelProperty(position = 3, required = true, value = "모델 영문 성((ex)CHO")
	private String modelFirstName;

	@NotNull(message = "모델 영문 이름 입력은 필수입니다.")
	@ApiModelProperty(position = 4, required = true, value = "모델 영문 이름((ex)CHAN HEE")
	private String modelSecondName;

	@NotNull(message = "모델 국문 성 입력은 필수입니다.")
	@ApiModelProperty(position = 5, required = true, value = "모델 국문 성((ex)조")
	private String modelKorFirstName;

	@NotNull(message = "모델 국문 이름 입력은 필수입니다.")
	@ApiModelProperty(position = 6, required = true, value = "모델 국문 이름((ex)찬희")
	private String modelKorSecondName;

	@ApiModelProperty(position = 13, value = "모델 경력 사항")
	private String career;

	@ApiModelProperty(position = 14, value = "모델 경력 사항")
	private ArrayList<careerJson> careerList;

	public AdminModelDTO toModel() {
		AdminModelDTO adminModelDTO = AdminModelDTO.builder()
				.categoryCd(this.categoryCd)
				.categoryAge(this.categoryAge)
				.modelKorName(this.modelKorName)
				.modelEngName(this.modelEngName)
				.modelDescription(this.modelDescription)
				.height(this.height)
				.size3(this.size3)
				.shoes(this.shoes)
				.visible(this.visible)
				.modelMainYn(this.modelMainYn)
				.modelFirstName(this.modelFirstName)
				.modelSecondName(this.modelSecondName)
				.modelKorFirstName(this.modelKorFirstName)
				.modelKorSecondName(this.modelKorSecondName)
				.career(this.career)
				.build();

		return adminModelDTO;
	}
}
