package com.bestarchitect.security;

public interface RequestPipelineFactory {
	public abstract void init();

	public abstract RequestProcessor getRequestProcessorPipeline();
}
