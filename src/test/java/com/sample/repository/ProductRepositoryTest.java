package com.sample.repository;

import com.sample.AbstractTest;
import com.sample.domain.Product;
import com.sample.web.security.SecurityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
//@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  //not needed as of now..
public class ProductRepositoryTest extends AbstractTest {

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void initialize() {
        SecurityUtils.runAs("user", "user", "ROLE_USER");
    }

    @Test
    public void findProductById() {

        Product product = this.productRepository.findOne(1L);
        assertNotNull(product);
        assertThat(product.getEanCode(), is(equalTo("1qazxswed")));

    }

    @After
    public void afterTest() {
        SecurityContextHolder.clearContext();
    }

}
