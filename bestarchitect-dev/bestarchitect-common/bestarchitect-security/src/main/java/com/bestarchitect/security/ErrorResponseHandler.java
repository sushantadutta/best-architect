package com.bestarchitect.security;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface ErrorResponseHandler {
	/**
	 * 
	 * @param request HttpServlet Request
	 * @param response HttpServlet response
	 * @param authException Authentication Exception
	 * @throws IOException
	 * @throws ServletException
	 */
	public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException;
}
