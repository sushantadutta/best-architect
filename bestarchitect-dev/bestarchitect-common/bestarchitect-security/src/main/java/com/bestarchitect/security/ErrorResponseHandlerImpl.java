package com.bestarchitect.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseHandlerImpl implements ErrorResponseHandler {

	private static final String FHIR_RESOURCE_URL = "/core/audit/AuditEvent/_search";

	private static final String RFC_RESOURCE_URL = "/store/audit/AuditEvent/_search";

	private ErrorResponseCreator errorResponseCreator;

	public ErrorResponseHandlerImpl(ErrorResponseCreator errorResponseCreator) {
		this.errorResponseCreator = errorResponseCreator;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		
	}

}
