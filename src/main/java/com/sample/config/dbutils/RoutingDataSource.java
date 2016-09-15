package com.sample.config.dbutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by himanshu.virmani on 06/09/16.
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("db look up key " + DbContextHolder.getDbType());
        return DbContextHolder.getDbType();
    }
}
