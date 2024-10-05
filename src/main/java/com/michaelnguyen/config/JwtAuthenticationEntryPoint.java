package com.michaelnguyen.config;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelnguyen.dto.response.ApiResponse;
import com.michaelnguyen.error.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
		response.setStatus(errorCode.getStatuscode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ApiResponse<?> apiResponse = ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage())
				.build();
		ObjectMapper objMapper = new ObjectMapper();

		response.getWriter().write(objMapper.writeValueAsString(apiResponse));
		response.flushBuffer();
	}

}
