package com.sample.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.AbstractControllerTest;
import com.sample.web.rest.dto.ProductResponseDto;
import com.sample.web.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * TODO
 * Explore on test utils  like EnvironmentTestUtils, ConfigFileApplicationContextInitializer, OutputCapture
 */
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
@Transactional
@Slf4j
public class ProductControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        super.setUp();
        SecurityUtils.runAs("user", "user", "ROLE_USER");
    }


    @Test
    public void testGetProductById() throws Exception {

        ResultActions s = mvc.perform(MockMvcRequestBuilders.get("/product/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ProductResponseDto productResponseDto = objectMapper.readValue(s.andReturn().getResponse().getContentAsString(), ProductResponseDto.class);
        assertThat(productResponseDto.getEanCode(), is(equalTo("1qazxswed")));
    }

    @After
    public void afterRun() {
        SecurityContextHolder.clearContext();
    }
}

