package com.tsp.new_tsp_project.api.admin.support.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "tsp_support")
public class AdminSupportEntity {

	@Transient
	private Integer rnum;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idx")
	private Integer idx;

	@Column(name = "support_name")
	@NotEmpty(message = "지원자 이름 입력은 필수입니다.")
	private String supportName;

	@Column(name = "support_height")
	@NotEmpty(message = "지원자 키 입력은 필수입니다.")
	private Integer supportHeight;

	@Column(name = "support_size3")
	@NotEmpty(message = "지원자 사이즈 입력은 필수입니다.")
	private String supportSize3;

	@Column(name = "support_instagram")
	private String supportInstagram;

	@Column(name = "support_phone")
	@NotEmpty(message = "지원자 휴대폰 번호 입력은 필수입니다.")
	private String supportPhone;

	@Column(name = "support_message")
	@NotEmpty(message = "지원자 상세 내용 입력은 필수입니다.")
	@Lob
	private String supportMessage;

	@Column(name = "visible")
	private String visible;

	@Column(name = "support_time")
	private Date supportTime;
}
