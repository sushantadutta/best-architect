package com.bestarchitect.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("docker")
public class BestPersistenceDockerConfig {

    private static final String DOCKER_AUDIT_DRIVER_CLASS_NAME = "audit.driverClassName";

    private static final String DOCKER_AUDIT_JDBC_URL = "audit.jdbcUrl";

    private static final String DOCKER_AUDIT_USER_NAME = "audit.username";

    private static final String DOCKER_AUDIT_PASS = "audit.password";

    /**
     * @param env
     * @return Data Source
     */
    @Primary
    @Bean(name = "auditDatasource")
    public DataSource auditDatasource(Environment env) {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(env.getProperty(DOCKER_AUDIT_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty(DOCKER_AUDIT_JDBC_URL));
        dataSource.setUsername(env.getProperty(DOCKER_AUDIT_USER_NAME));
        dataSource.setPassword(env.getProperty(DOCKER_AUDIT_PASS));
        dataSource.setConnectionProperties(
                "sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8;singleton=true;show_sql=true");
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(5);
        dataSource.setMinIdle(2);
        return dataSource;
    }

    /**
     * @param env
     * @return Jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("auditDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
