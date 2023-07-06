package com.jiva.app.db.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "synchronydb.datasource")
@Configuration
public class SynchronyDriverConfig {
	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClassName;
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	@Override
	public String toString() {
		return "SynchronyDriverConfig [jdbcUrl=" + jdbcUrl + ", username=" + username + ", password=" + password
				+ ", driverClassName=" + driverClassName + "]";
	}
	
}
