package com.bestarchitect.security;

public interface ErrorResponseCreator {

	/**
	 * 
	 * @param code
	 * @param message
	 * @param severity
	 * @param status
	 * @return
	 */
	public String createErrorResponse(String code, String issueType, String message, String severity);
	/**
	 * 
	 * @param code
	 * @param issueType
	 * @param message
	 * @param severity
	 * @param system
	 * @return
	 */
	public String createErrorResponse(String code, String issueType, String message, String severity, String system);

}
