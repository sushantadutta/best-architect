package com.bestarchitect.createservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FunctionalInterface
@RequestMapping("/profile/")
public interface BestArchitectCreateController<T> {
    @RequestMapping(value = "create", method = RequestMethod.POST)
    ResponseEntity<?> create(T incomingMessage, String apiVersion);
}
