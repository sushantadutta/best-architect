package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.createservice.constant.CreateServiceConstants;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ContentTypeProcessor extends RequestProcessor {

    public static final String APPLICATION_JSON_CHARSET_ISO_8859_1 = "application/json;charset=ISO-8859-1";
    private static final String WILDCARDCHAR = "*";

    @Override
    protected boolean isValid(HttpServletRequest request, HttpServletResponse response) {
        final String contentTypeHeaderValue = getContentTypeFromRequest(request);

        if (contentTypeHeaderValue == null) {
            return false;
        }

        if (contentTypeHeaderValue.contains(WILDCARDCHAR)) {
            return false;
        }
        return contentTypeHeaderValue.equals(MediaType.APPLICATION_JSON_VALUE) || contentTypeHeaderValue.equals(MediaType.APPLICATION_JSON_UTF8_VALUE);

    }

    @Override
    protected void handleValidationFailed(HttpServletRequest request, HttpServletResponse response) {
        sendErrorResponse(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, CreateServiceConstants.INVALID,
                CreateServiceConstants.MSG_UNKNOWN_CONTENT, CreateServiceConstants.MEDIA_TYPE_NOT_SUPPORTED,
                CreateServiceConstants.ERROR);
    }

    private String getContentTypeFromRequest(final HttpServletRequest request) {
        return request.getHeader(HttpHeaders.CONTENT_TYPE);
    }
}
