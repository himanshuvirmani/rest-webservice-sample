package com.sample.service.impl;

import com.sample.domain.Aggregate;
import com.sample.repository.AggregateRepository;
import com.sample.service.AggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("aggregateService")
@Transactional
public class AggregateServiceImpl implements AggregateService {

    private final AggregateRepository cityRepository;

/*    @Autowired
    private HashCacheRedisRepository<Aggregate> valueCacheRedisRepository;*/

    @Autowired
    public AggregateServiceImpl(AggregateRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override
    public Aggregate getAggregateByExternalId(String externalId) {
        Assert.notNull(externalId, "Name must not be null");
//        Aggregate city = valueCacheRedisRepository.multiGet("city:" + name + ":country:" + country, Aggregate.class);
        Aggregate city = cityRepository.findByExternalId(externalId);
           /* if (city != null) {
                valueCacheRedisRepository.multiPut("city:" + name + ":country:" + country, city);
                valueCacheRedisRepository.expire("city:" + name + ":country:" + country, 60, TimeUnit.SECONDS);
            }*/
        return city;
    }

}
