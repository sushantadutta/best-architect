package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.createservice.constant.CreateServiceConstants;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AcceptHeaderProcessor extends RequestProcessor {

    private static final Logger LOG = LogManager.getLogger(AcceptHeaderProcessor.class);

    private static final String ACCEPT_TYPE = "application/json";

    private static final String ACCEPT_DEFAULT_TYPE = "*/*";

    @Override
    protected boolean isValid(HttpServletRequest request, HttpServletResponse response) {
        String acceptHeader = getAcceptHeader(request);
        LOG.info("Received accept header {} ", acceptHeader);
        if (acceptHeader == null) {
            return true;
        }

        return acceptHeader.equals(ACCEPT_TYPE) || acceptHeader.equals(ACCEPT_DEFAULT_TYPE);
    }

    @Override
    protected void handleValidationFailed(HttpServletRequest request, HttpServletResponse response) {
        sendErrorResponse(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, CreateServiceConstants.NOT_SUPPORTED,
                CreateServiceConstants.MSG_UNKNOWN_CONTENT, CreateServiceConstants.MEDIA_TYPE_NOT_SUPPORTED,
                CreateServiceConstants.ERROR);

    }

    private String getAcceptHeader(HttpServletRequest request) {
        return request.getHeader("Accept");
    }
}
