/**
 * 
 */
package com.bestarchitect.createservice;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Sushanta
 *
 */
public class BestArchitectCreateApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BestArchitectCreateApplication.class);
		context.registerShutdownHook();

	}

}
