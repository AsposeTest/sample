package com.asposetest.sample.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.asposetest.sample.support.AspsoeTestException;
import com.asposetest.sample.support.SupportUtils;

/**
 * ExceptionController is a controller advice class used as an ErrorHandler
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> showErrorPage(HttpServletRequest request, Exception exception) {
		SupportUtils.printSysout(exception.getMessage());
		Throwable errorCause = exception.getCause();
		SupportUtils.printSysout(errorCause == null ? "cause = NULL" : errorCause.toString());

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		return getErrorResponse(httpStatus.value(), "ERROR : " + exception.getMessage());
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ResponseEntity<String> showInvalidUrl() {
		HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		return getErrorResponse(httpStatus.value(), "Invalid URL - 404 Not Found");
	}

	@ExceptionHandler(value = AspsoeTestException.class)
	public ResponseEntity<String> showCustomException(AspsoeTestException aspsoeTestException) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		return getErrorResponse(httpStatus.value(), aspsoeTestException.getAsposeTestErrorMsg());
	}

	private ResponseEntity<String> getErrorResponse(int statusCode, String errorMessage) {
		return ResponseEntity.status(statusCode).body(errorMessage);
	}
}