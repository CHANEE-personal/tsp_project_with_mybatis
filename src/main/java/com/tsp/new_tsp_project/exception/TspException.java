package com.tsp.new_tsp_project.exception;

import lombok.Getter;

public class TspException extends RuntimeException {

	@Getter
	private BaseExceptionType baseExceptionType;

	public TspException(BaseExceptionType baseExceptionType) {
		super(baseExceptionType.getErrorMessage());
		this.baseExceptionType = baseExceptionType;
	}
}
