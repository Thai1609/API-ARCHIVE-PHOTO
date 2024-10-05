package com.michaelnguyen.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.michaelnguyen.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse> handingAppException(AppException exception) {

		ErrorCode errorCode = exception.getErrorCode();

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(exception.getMessage());

		return ResponseEntity.status(errorCode.getStatuscode()).body(apiResponse);

	}

	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse> handingAppAccessDeniedException(AccessDeniedException exception) {

		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

		return ResponseEntity.status(errorCode.getStatuscode())
				.body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());

	}

}
