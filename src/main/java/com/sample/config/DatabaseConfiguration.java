package com.sample.config;

import com.sample.common.AuditingDateTimeProvider;
import com.sample.common.DateTimeService;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.sample.config.dbutils.DbType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories("com.sample.repository")
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private RelaxedPropertyResolver dataSourcePropertyResolver;
    private RelaxedPropertyResolver slaveDataSourcePropertyResolver;

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.dataSourcePropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        this.slaveDataSourcePropertyResolver = new RelaxedPropertyResolver(env, "spring.slavedatasource.");
    }

    @Bean
    public DataSource masterDataSource() {
        log.debug("Configuring Master Datasource");
        if (dataSourcePropertyResolver.getProperty("url") == null
                && dataSourcePropertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            " cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourcePropertyResolver.getProperty("dataSourceClassName"));
        if (StringUtils.isEmpty(dataSourcePropertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName",
                    dataSourcePropertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName",
                    dataSourcePropertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", dataSourcePropertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", dataSourcePropertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", dataSourcePropertyResolver.getProperty("password"));

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(
                dataSourcePropertyResolver.getProperty("dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts",
                    dataSourcePropertyResolver.getProperty("cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize",
                    dataSourcePropertyResolver.getProperty("prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourcePropertyResolver
                    .getProperty("prepStmtCacheSqlLimit", "2048"));
        }
        return new HikariDataSource(config);
    }

    @Bean
    public DataSource slaveDataSource() {
        log.debug("Configuring Slave Datasource");
        if (slaveDataSourcePropertyResolver.getProperty("url") == null
                && slaveDataSourcePropertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            " cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(slaveDataSourcePropertyResolver.getProperty("dataSourceClassName"));
        if (StringUtils.isEmpty(slaveDataSourcePropertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName",
                    slaveDataSourcePropertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName",
                    slaveDataSourcePropertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", slaveDataSourcePropertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", slaveDataSourcePropertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", slaveDataSourcePropertyResolver.getProperty("password"));

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(
                slaveDataSourcePropertyResolver.getProperty("dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts",
                    slaveDataSourcePropertyResolver.getProperty("cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize",
                    slaveDataSourcePropertyResolver.getProperty("prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", slaveDataSourcePropertyResolver
                    .getProperty("prepStmtCacheSqlLimit", "2048"));
        }
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbType.MASTER, masterDataSource());
        targetDataSources.put(DbType.SLAVE, slaveDataSource());

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());

        return routingDataSource;
    }

    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }

    @Bean
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Bean
    DateTimeProvider constantDateTimeProvider(DateTimeService constantDateTimeService) {
        return new AuditingDateTimeProvider(constantDateTimeService);
    }
}
