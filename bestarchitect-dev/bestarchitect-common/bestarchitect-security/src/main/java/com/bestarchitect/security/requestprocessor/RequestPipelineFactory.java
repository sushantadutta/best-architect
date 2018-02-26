package com.bestarchitect.security.requestprocessor;

public interface RequestPipelineFactory {
    void init();

    RequestProcessor getRequestProcessorPipeline();
}
