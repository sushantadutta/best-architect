package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.createservice.constant.CreateServiceConstants;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CheckSizeProcessor extends RequestProcessor {

    private static final Logger LOG = LogManager.getLogger(CheckSizeProcessor.class);
    private int payloadSize;

    public CheckSizeProcessor(int payloadSize) {
        this.payloadSize = payloadSize;
    }

    @Override
    protected boolean isValid(HttpServletRequest request, HttpServletResponse response) {
        int contentLength = request.getContentLength();
        double kilobytes = contentLength / (double) 1024;
        double megabytes = kilobytes / (double) 1024;
        return !(megabytes > payloadSize);
    }

    @Override
    protected void handleValidationFailed(HttpServletRequest request, HttpServletResponse response) {
        String apiVersion = request.getHeader("api-version");
        if (apiVersion == null) {
            response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
            try {
                response.getOutputStream()
                        .write(CreateServiceConstants.APP_TOO_LONG_PAYLOAD.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                LOG.error("Error in sending the response " + e, e);
            }

        } else {
            sendErrorResponse(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    CreateServiceConstants.TOO_LONG, CreateServiceConstants.MSG_UNKNOWN_CONTENT,
                    CreateServiceConstants.APP_TOO_LONG_PAYLOAD, CreateServiceConstants.ERROR);

        }
    }

}
