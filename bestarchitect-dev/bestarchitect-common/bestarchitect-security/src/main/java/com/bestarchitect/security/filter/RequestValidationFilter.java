package com.bestarchitect.security.filter;

import com.bestarchitect.security.requestprocessor.RequestPipelineFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestValidationFilter extends OncePerRequestFilter {
	private final RequestPipelineFactory requestPipelineFactory;

	public RequestValidationFilter(final RequestPipelineFactory requestPipelineFactory) {
		this.requestPipelineFactory = requestPipelineFactory;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {
		final boolean isValidRequest = requestPipelineFactory.getRequestProcessorPipeline().process(request, response);
		if (isValidRequest) {
			filterChain.doFilter(request, response);
		}
	}
}
