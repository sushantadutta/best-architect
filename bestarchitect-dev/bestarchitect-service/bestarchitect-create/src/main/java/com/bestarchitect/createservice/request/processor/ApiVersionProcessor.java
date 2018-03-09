package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.createservice.constant.CreateServiceConstants;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiVersionProcessor extends RequestProcessor {

    private static final String SLASH = "/";
    private static final String API_VERSION_VALUE = "1";

    @Override
    protected boolean isValid(HttpServletRequest request, HttpServletResponse response) {
        String path = stripTrailingSlashesFromPath(request.getServletPath());
        if (path.equals(CreateServiceConstants.CREATE_RESOURCE_URL)) {
            String apiVersion = request.getHeader("api-version");
            return apiVersion != null && apiVersion.equals(API_VERSION_VALUE);

        }
        return true;
    }

    @Override
    protected void handleValidationFailed(HttpServletRequest request, HttpServletResponse response) {
    }

    String stripTrailingSlashesFromPath(final String path) {
        return StringUtils.stripEnd(path, SLASH);
    }

}
