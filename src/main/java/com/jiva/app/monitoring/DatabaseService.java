package com.jiva.app.monitoring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

@Component("Database")
public class DatabaseService implements HealthIndicator{

	
	@Autowired
	@Qualifier("synchroyTemplate")
	private JdbcTemplate synchronyTemplate;
	
	
	private final String DATABASE_SERVICE = "Database service";
	@Override
	public Health health() {
		if(isDatabaseHealthGood()) {
			return Health.up().withDetail(DATABASE_SERVICE, "Service is running").build();
		}
		return Health.down().withDetail(DATABASE_SERVICE, "Service is not available").build();
	}
	
	private boolean isDatabaseHealthGood() {
		 List<Object> results = synchronyTemplate.query("select 1", new SingleColumnRowMapper<>());
		 if(results.size() == 1)
			 return true;
		 return false;
	}

}
