package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.createservice.constant.CreateServiceConstants;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpMethodAndResourceValidationProcessor extends RequestProcessor {

    private final Map<String, List<HttpMethod>> allowedMethodsPerResource = new HashMap<>();

    public HttpMethodAndResourceValidationProcessor() {
        allowedMethodsPerResource.put(CreateServiceConstants.FHIR_CREATE_RESOURCE_URL,
                Arrays.asList(HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.HEAD));
        allowedMethodsPerResource.put(CreateServiceConstants.RFC_CREATE_RESOURCE_URL,
                Arrays.asList(HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.HEAD));
    }

    @Override
    protected boolean isValid(final HttpServletRequest request, final HttpServletResponse response) {
        final String method = request.getMethod();
        return StringUtils.isNotBlank(method)
                && isAllowedMethod(request.getServletPath(), method);
    }

    boolean isAllowedMethod(final String path, final String method) {
        final List<HttpMethod> httpMethods = allowedMethodsPerResource.get(path);
        if (httpMethods == null) {
            return false;
        }
        for (HttpMethod allowedMethod : httpMethods) {
            if (allowedMethod.matches(method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void handleValidationFailed(final HttpServletRequest request, final HttpServletResponse response) {
        final List<HttpMethod> allowed = allowedMethodsPerResource
                .get(request.getServletPath());
        if (allowed == null) {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, CreateServiceConstants.INVALID,
                    CreateServiceConstants.RESOURCE_NOT_FOUND, CreateServiceConstants.RESOURCE_NOT_FOUND,
                    CreateServiceConstants.ERROR);
        } else {
            response.addHeader(HttpHeaders.ALLOW, StringUtils.join(allowed.toArray(), ", "));
            sendErrorResponse(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, CreateServiceConstants.NOT_SUPPORTED,
                    CreateServiceConstants.MSG_OP_NOT_ALLOWED, CreateServiceConstants.METHOD_NOT_SUPPORTED,
                    CreateServiceConstants.ERROR);
        }
    }
}
