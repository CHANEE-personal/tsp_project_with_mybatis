package com.tsp.new_tsp_project.exception;

public interface BaseExceptionType {

	// errorCode
	String getErrorCode();
	// HttpStatus
	int getHttpStatus();
	// errorMessage
	String getErrorMessage();
}
