package com.bestarchitect.security.handler;

import org.springframework.stereotype.Component;

@Component
public class OperationOutcomeErrorResponseCreator implements ErrorResponseCreator {

	private static final String SYSTEM = "https://www.hl7.org/fhir/valueset-operation-outcome.html";
	private static final String RESOURCE_TYPE = "OperationOutcome";

	@Override
	public String createErrorResponse(String code, String issueType, String message, String severity, String system) {
		return "";
	}

	@Override
	public String createErrorResponse(String code, String issueType, String message, String severity) {
		return createErrorResponse(code, issueType, message, severity, SYSTEM);
	}

}
