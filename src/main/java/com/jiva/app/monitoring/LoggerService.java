package com.jiva.app.monitoring;


import java.util.List;

import org.apache.logging.slf4j.SLF4JLoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class LoggerService implements HealthIndicator{

	private final String LOGGER_SERVICE = "Logger service";
	
	@Override
	public Health health() {
		if(isLoggerServiceUp()) {
			return Health.up().withDetail(LOGGER_SERVICE, "Service is up").build();
		}
		return Health.down().withDetail(LOGGER_SERVICE, "Service is not available").build();
	}
	
	private boolean isLoggerServiceUp() {
		/// Check if the logging framework is initialized
		Logger logger = (Logger) LoggerFactory.getILoggerFactory();
		if(logger.isErrorEnabled())
			return true;
		return false;
	}
}
