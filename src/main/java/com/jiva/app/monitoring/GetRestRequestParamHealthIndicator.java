package com.jiva.app.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GetRestRequestParamHealthIndicator implements HealthIndicator{
	
	
	String url = "http://localhost:8080/JivaApiHandler/api/v1/getClinicUserName?groupName=DLF";
	private final RestTemplate restTemplate;

	public GetRestRequestParamHealthIndicator(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	@Override
	public Health health() {
		 try {
	            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
	            if (response.getStatusCode().is2xxSuccessful()) {
	                return Health.up().withDetail("message", "GET REST service with RequestParam is up").build();
	            } else {
	                return Health.down().withDetail("message", "GET REST service with RequestParam is down").build();
	            }
	        } catch (Exception e) {
	            return Health.down().withDetail("message", "GET REST service with RequestParam is down").build();
	        }
	}

}
