package com.bestarchitect.createservice.request.processor;

import com.bestarchitect.security.handler.ErrorResponseCreator;
import com.bestarchitect.security.requestprocessor.HttpsValidationProcessor;
import com.bestarchitect.security.requestprocessor.RequestPipelineFactory;
import com.bestarchitect.security.requestprocessor.RequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RestRequestPipeLineFactory implements RequestPipelineFactory {

    @Value("${upgrade_scheme_value}")
    private boolean checkForHttpsAccess;

    @Value("${payload_size}")
    private int payLoadSize;

    private RequestProcessor pipeline;

    private ErrorResponseCreator errorResponseCreator;


    @Autowired
    public RestRequestPipeLineFactory(ErrorResponseCreator errorResponseCreator) {
        this.errorResponseCreator = errorResponseCreator;
    }

    @Override
    @PostConstruct
    public void init() {
        final CheckSizeProcessor checkSizeProcessor = new CheckSizeProcessor(payLoadSize);
        checkSizeProcessor.setErrorResponseCreator(errorResponseCreator);

        final ApiVersionProcessor apiVersionProcessor = new ApiVersionProcessor();
        apiVersionProcessor.setErrorResponseCreator(errorResponseCreator);
        apiVersionProcessor.setNextProcessor(checkSizeProcessor);

        final AcceptHeaderProcessor acceptHeaderProcessor = new AcceptHeaderProcessor();
        acceptHeaderProcessor.setErrorResponseCreator(errorResponseCreator);
        acceptHeaderProcessor.setNextProcessor(apiVersionProcessor);

        final ContentTypeProcessor contentTypeProcessor = new ContentTypeProcessor();
        contentTypeProcessor.setErrorResponseCreator(errorResponseCreator);
        contentTypeProcessor.setNextProcessor(acceptHeaderProcessor);

        final HttpsValidationProcessor httpsValidationProcessor = new HttpsValidationProcessor();
        httpsValidationProcessor.setErrorResponseCreator(errorResponseCreator);
        httpsValidationProcessor.setCheckForHttpsAccess(checkForHttpsAccess);
        httpsValidationProcessor.setNextProcessor(contentTypeProcessor);

        final HttpMethodAndResourceValidationProcessor httpMethodValidationProcessor = new HttpMethodAndResourceValidationProcessor();
        httpMethodValidationProcessor.setNextProcessor(httpsValidationProcessor);
        httpMethodValidationProcessor.setErrorResponseCreator(errorResponseCreator);
        pipeline = httpMethodValidationProcessor;
    }

    @Override
    public RequestProcessor getRequestProcessorPipeline() {
        return pipeline;
    }

}
