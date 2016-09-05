package com.sample.service;

import com.sample.AbstractTest;
import com.sample.domain.Product;
import com.sample.helper.ProductServiceHelper;
import com.sample.repository.CategoryRepository;
import com.sample.repository.ProductRepository;
import com.sample.service.impl.ProductServiceImpl;
import com.sample.util.ProductTestUtil;
import com.sample.web.rest.dto.ProductResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

/**
 * Created by himanshu.virmani on 20/09/15.
 */
public class ProductServiceTest extends AbstractTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductServiceHelper productServiceHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productRepository, categoryRepository, productServiceHelper);
    }

    @Test
    public void testAddProducts() {
        List<Product> dummyproductList = ProductTestUtil.createProductListData(1L, "DummyProd", new BigDecimal(10), 1L, "Household", 12D, "Pieces", "asdfgh");
        when(productRepository.save(dummyproductList)).thenReturn(dummyproductList);
        when(categoryRepository.findOne(1L)).thenReturn(ProductTestUtil.createDummyCategory(1L, "Household"));
        List<ProductResponseDto> productResponseDtos = productService.addProducts(ProductTestUtil.createProductRequest("DummyProd",new BigDecimal(10),1L,12D,"Pieces","asdfgh"));
        assertNotNull(productResponseDtos);
        assertThat(productResponseDtos.size(), is(equalTo(1)));
        assertThat(productResponseDtos.get(0).getCategory(), is(equalTo("Household")));
    }

}
