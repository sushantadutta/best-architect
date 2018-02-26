package com.bestarchitect.security.requestprocessor;


import com.bestarchitect.security.constant.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HttpsValidationProcessor extends RequestProcessor {

	private boolean checkForHttpsAccess;

	@Override
	protected boolean isValid(final HttpServletRequest request, final HttpServletResponse response) {
		if (checkForHttpsAccess) {
			final String xForwardedPortHeaderValue = request.getHeader(SecurityConstants.HEADER_X_FORWARDED_PORT);
			final String xForwardedProtocolHeaderValue = request
					.getHeader(SecurityConstants.HEADER_X_FORWARDED_PROTOCOL);

			return requestForwardedFromHttpsPort(xForwardedPortHeaderValue)
					&& requestForwardedFromHttpsProtocol(xForwardedProtocolHeaderValue);
		}
		return true;
	}

	private boolean requestForwardedFromHttpsPort(final String xForwardedPort) {
		return SecurityConstants.HTTPS_PORT.equals(xForwardedPort);
	}

	private boolean requestForwardedFromHttpsProtocol(final String xForwardedProtocol) {
		return SecurityConstants.HTTPS_PROTOCOL.equalsIgnoreCase(xForwardedProtocol);
	}

	@Override
	protected void handleValidationFailed(final HttpServletRequest request, final HttpServletResponse response) {
		sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "forbidden", "MSG_UNKNOWN_OPERATION",
				"HTTP Scheme not supported", "error");
	}

	public void setCheckForHttpsAccess(final boolean checkForHttpsAccess) {
		this.checkForHttpsAccess = checkForHttpsAccess;
	}

}
