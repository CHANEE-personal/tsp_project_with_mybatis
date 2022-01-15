package com.tsp.new_tsp_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	static class Error {
		private String code;
		private int status;
		private String message;

		static Error create(BaseExceptionType exception) {
			return new Error(exception.getErrorCode(), exception.getHttpStatus(), exception.getErrorMessage());
		}
	}

	@ExceptionHandler({TspException.class})
	public ResponseEntity<Error> exception(TspException tspException) {
		return new ResponseEntity<>(Error.create(tspException.getBaseExceptionType()), HttpStatus.OK);
	}

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status) {
//		Map<String, Object> body = new HashMap<>();
//		body.put("timestamp", new Date());
//		body.put("status", status.value());
//
//		List<String> errors = ex.getBindingResult()
//				.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
//				.collect(Collectors.toList());
//
//		body.put("errors", errors);
//
//		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, body);
//
//		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//
//	}

	/**
	 * <pre>
	 * 1. MethodName : handleConstraintViolation
	 * 2. ClassName  : ApiExceptionHandler.java
	 * 3. Comment    : Parameter Validation Check
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 15.
	 * </pre>
	 *
	 * @param e
	 * @param request
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
		return handleExceptionInternal(e, messageSource.getMessage("modelCategory.Range", new String[]{}, Locale.KOREA), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * <pre>
	 * 1. MethodName : handleMethodArgumentNotValid
	 * 2. ClassName  : ApiExceptionHandler.java
	 * 3. Comment    : Entity or DTO Validation Check
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 15.
	 * </pre>
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		for (ObjectError error : allErrors) {
			String message = Arrays.stream(Objects.requireNonNull(error.getCodes()))
					.map(c -> {
						Object[] arguments = error.getArguments();
						Locale locale = LocaleContextHolder.getLocale();
						try {
							return messageSource.getMessage(c, arguments, locale);
						} catch (NoSuchMessageException e) {
							return null;
						}
					}).filter(Objects::nonNull)
					.findFirst()
					// 코드를 찾지 못할 경우 기본 메시지 사용.
					.orElse(error.getDefaultMessage());

			log.error("error messages: {}", message);
		}
		return super.handleMethodArgumentNotValid(ex, headers, status, request);
	}
}
