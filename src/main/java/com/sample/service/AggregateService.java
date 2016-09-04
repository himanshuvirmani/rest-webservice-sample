package com.sample.service;

import com.sample.domain.Aggregate;

public interface AggregateService {

    Aggregate getAggregateByExternalId(String externalId);

}
