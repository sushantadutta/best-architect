package com.bestarchitect.createservice.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.bestarchitect.createservice.request.processor",
        "com.bestarchitect.createservice.controller",
        "com.bestarchitect.security",
        "com.bestarchitect.persistence",
        "com.bestarchitect.logging",
        "com.bestarchitect.messaging"})
public class CreateServiceConfig extends WebMvcAutoConfiguration {


}



