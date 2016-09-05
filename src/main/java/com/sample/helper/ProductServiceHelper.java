package com.sample.helper;

import com.sample.web.rest.dto.FixerResponseDto;
import com.sample.web.rest.dto.ProductRequestDto;
import com.sample.web.rest.errors.CurrencyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by himanshu.virmani on 05/09/16.
 */
@Slf4j
@Component("productServiceHelper")
@EnableConfigurationProperties
@ConfigurationProperties
public class ProductServiceHelper {

    @Value("${envConfig.fixerServiceUrl}")
    private String fixerServiceUrl;

    public boolean isCurrencyConversionsRequired(List<ProductRequestDto> productDtos) {
        return productDtos.stream().filter(productRequestDto -> productRequestDto.getCurrency() != null).count() > 0;
    }

    public Map<String, BigDecimal> getCurrencyMap() {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fixerServiceUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(null, httpHeaders);
        ResponseEntity<FixerResponseDto> responseEntity = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity, FixerResponseDto.class);
        log.debug("Fixer Response : " + responseEntity.getBody());
        return responseEntity.getBody().getRates();
    }

    // Using parallel stream for the first time..Lets see how it works out.
    public void doCurrencyConversions(List<ProductRequestDto> productDtos, Map<String, BigDecimal> currencyMap) {
        productDtos.parallelStream().map(productRequestDto1 -> {
            String currency = productRequestDto1.getCurrency();
            if (currency != null) {
                if (currencyMap.get(currency) == null)
                    throw new CurrencyNotFoundException("Currency with name " + currency + "not found");
                BigDecimal newPrice = productRequestDto1.getPrice().divide(currencyMap.get(currency), RoundingMode.HALF_UP);
                productRequestDto1.setPrice(newPrice);
            }
            return productRequestDto1;
        }).collect(Collectors.toList());
    }
}
