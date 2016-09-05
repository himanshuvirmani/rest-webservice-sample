package com.sample.web.rest.dto;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by himanshu.virmani on 05/09/16.
 */
@Data
public class FixerResponseDto {

    private String base;
    private DateTime date;
    private Map<String,BigDecimal> rates;

}
