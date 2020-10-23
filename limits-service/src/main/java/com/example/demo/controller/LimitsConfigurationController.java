package com.example.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.LimitConfiguration;
import com.example.demo.configuration.Configuration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RefreshScope

public class LimitsConfigurationController {
	
	@Autowired
	private Configuration config;
	
	@GetMapping("/limits")
	@HystrixCommand(fallbackMethod = "fallbackValue")
	public LimitConfiguration retriveLimitsFromConfigurations() throws Exception
	{
		Random random = new Random();
		if(random.nextBoolean())
		{
		
		return new LimitConfiguration(config.getMaximum(), config.getMinimum());
		}else {
			throw new Exception("Iam a garbage please leave me");
		}
	}
    
	@HystrixCommand(fallbackMethod = "fallbackValue")
	public LimitConfiguration fallbackValue()
	{
		return new LimitConfiguration(77, 77777);
	}
}
