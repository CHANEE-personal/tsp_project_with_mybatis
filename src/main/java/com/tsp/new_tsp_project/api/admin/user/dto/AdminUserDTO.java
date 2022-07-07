package com.tsp.new_tsp_project.api.admin.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tsp.new_tsp_project.api.common.domain.dto.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AdminUserDTO extends NewCommonDTO {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	Integer rnum;

	@ApiModelProperty(required = true, value = "user Seq", hidden = true)
	Integer idx;

	@ApiModelProperty(required = true, value = "user Id")
	String userId;

	@ApiModelProperty(required = true, value = "user Password")
	String password;

	@ApiModelProperty(required = true, value = "user Name", hidden = true)
	String name;

	@ApiModelProperty(required = true, value = "user email", hidden = true)
	String email;

	@ApiModelProperty(required = true, value = "user visible", hidden = true)
	String visible;

	@ApiModelProperty(value = "user Token", hidden = true)
	String userToken;

	@ApiModelProperty(value = "user refresh Token", hidden = true)
	String userRefreshToken;

	@Enumerated(value = STRING)
	private Role role;
}
