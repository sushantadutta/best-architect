package com.bestarchitect.persistence;

import com.bestarchitect.persistence.constant.BestArchitectPersistenceConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.cloud.service.relational.DataSourceConfig.ConnectionConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Profile(value = {"cloud", "default"})
public class BestArchitectPersistenceConfig extends AbstractCloudConfig {

    /**
     * @param env
     * @return Data Source
     */
    @Primary
    @Bean(name = "auditDatasource")
    public DataSource auditDatasource(Environment env, Cloud cloud) {
        ConnectionConfig connectionConfig = new ConnectionConfig(
                "sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8;singleton=true;show_sql=true");
        int maxConnectionPool = env.getProperty(BestArchitectPersistenceConstants.MAX_CONNECTION_POOL_SIZE) == null ? 10
                : Integer.parseInt(env.getProperty(BestArchitectPersistenceConstants.MAX_CONNECTION_POOL_SIZE));
        PoolConfig poolConfig = new PoolConfig(maxConnectionPool, 200);
        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
        String serviceName = "";
        for (ServiceInfo serviceInfo : serviceInfos) {
            if (serviceInfo.getId().startsWith(BestArchitectPersistenceConstants.AUDIT_DATABASE_PREFIX)) {
                serviceName = serviceInfo.getId();
            }
        }
        return connectionFactory().dataSource(serviceName, new DataSourceConfig(poolConfig, connectionConfig));
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