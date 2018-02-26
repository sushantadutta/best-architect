package com.bestarchitect.security;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public abstract class RequestProcessor {

	private static final String DEFAULT_MESSAGE = "Request Validation failed. Returning %d :: [%s]";
	private static final Logger LOG = LogManager.getLogger(RequestProcessor.class);

	private RequestProcessor nextProcessor;

	private ErrorResponseCreator errorResponseCreator;

	private List<HttpMethod> applicableMethods;

	public boolean process(final HttpServletRequest request, final HttpServletResponse response) {
		boolean isValid = true;
		if (processorAppliesForRequest(request)) {
			isValid = isValid(request, response);
		}

		if (!isValid) {
			handleValidationFailed(request, response);
		} else if (nextProcessor != null) {
			isValid = nextProcessor.process(request, response);
		}

		return isValid;
	}

	private boolean processorAppliesForRequest(final HttpServletRequest request) {
		if (applicableMethods == null) {
			return true;
		}
		for (HttpMethod method : applicableMethods) {
			if (method.matches(request.getMethod())) {
				return true;
			}
		}
		return false;
	}

	protected abstract boolean isValid(HttpServletRequest request, HttpServletResponse response);

	protected abstract void handleValidationFailed(HttpServletRequest request, HttpServletResponse response);

	protected void sendErrorResponse(final HttpServletResponse response, final int httpStatusCode, final String code,
			final String issueType, final String message, final String severity) {
		LOG.warn(String.format(DEFAULT_MESSAGE, httpStatusCode, message));

		String responseBody = null;
		if (errorResponseCreator != null) {
			responseBody = errorResponseCreator.createErrorResponse(code, issueType, message, severity);
		}

		if (responseBody != null) {
			try {
				response.getOutputStream().write(responseBody.getBytes(StandardCharsets.UTF_8));
			} catch (final Exception ex) {
				LOG.error("Failed to send a response body. Sending without a body.", ex);
			}
		}
		response.setHeader("Server", "N/A");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(httpStatusCode);

	}

	public void setNextProcessor(final RequestProcessor nextProcessor) {
		this.nextProcessor = nextProcessor;
	}

	public void setErrorResponseCreator(final ErrorResponseCreator errorResponseCreator) {
		this.errorResponseCreator = errorResponseCreator;
	}

	public void setApplicableMethods(final List<HttpMethod> applicableMethods) {
		this.applicableMethods = applicableMethods;
	}

	public RequestProcessor getNextProcessor() {
		return nextProcessor;
	}

}
