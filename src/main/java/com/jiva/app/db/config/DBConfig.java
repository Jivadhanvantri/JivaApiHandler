package com.jiva.app.db.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;
import javax.sql.DataSource;
@Configuration
@EnableTransactionManagement
public class DBConfig {
	
	@Autowired
	private SynchronyDriverConfig synchronyConfig;
	
	@Autowired
	private JivaDriverConfig jivaConfig;
	
	@Autowired
	private NiceDriverConfig niceConfig;	
	
	@Bean(name = { "db1" })
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource1() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(synchronyConfig.getJdbcUrl());
        hikariConfig.setUsername(synchronyConfig.getUsername());
        hikariConfig.setPassword(synchronyConfig.getPassword());
        hikariConfig.setDriverClassName(synchronyConfig.getDriverClassName());
        hikariConfig.setConnectionTimeout(20000L);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTestQuery("select 1");
        hikariConfig.setMaximumPoolSize(6);
        hikariConfig.setIdleTimeout(10000L);
        hikariConfig.setMaxLifetime(1000L);
        return new HikariDataSource(hikariConfig);
    }
	
	@Bean(name = { "db2" })
    @ConfigurationProperties(prefix = "jiva.datasource")
	public DataSource dataSource2() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jivaConfig.getJdbcUrl());
        hikariConfig.setUsername(jivaConfig.getUsername());
        hikariConfig.setPassword(jivaConfig.getPassword());
        hikariConfig.setDriverClassName(jivaConfig.getDriverClassName());
        hikariConfig.setConnectionTimeout(20000L);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTestQuery("select 1");
        hikariConfig.setMaximumPoolSize(2);
        hikariConfig.setIdleTimeout(10000L);
        hikariConfig.setMaxLifetime(1000L);
        return new HikariDataSource(hikariConfig);
    }
	
	@Bean(name = { "db5" })
    @ConfigurationProperties(prefix = "nice.datasource")
	public DataSource dataSource5() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(niceConfig.getJdbcUrl());
        hikariConfig.setUsername(niceConfig.getUsername());
        hikariConfig.setPassword(niceConfig.getPassword());
        hikariConfig.setDriverClassName(niceConfig.getDriverClassName());
        hikariConfig.setConnectionTimeout(20000L);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTestQuery("select 1");
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setIdleTimeout(10000L);
        hikariConfig.setMaxLifetime(1000L);
        return new HikariDataSource(hikariConfig);
    }
	
	 @Bean(name = { "synchroyTemplate" })
	    public JdbcTemplate jdbcTemplate1(@Qualifier("db1") final DataSource ds) {
	        return new JdbcTemplate(ds);
	  }
	 
	 @Bean(name = { "jivaTemplate" })
	    public JdbcTemplate jdbcTemplate2(@Qualifier("db2") final DataSource ds) {
	        return new JdbcTemplate(ds);
	  }
	 
	 @Bean(name = { "niceTemplate" })
	    public JdbcTemplate jdbcTemplate5(@Qualifier("db5") final DataSource ds) {
	        return new JdbcTemplate(ds);
	  }
}

