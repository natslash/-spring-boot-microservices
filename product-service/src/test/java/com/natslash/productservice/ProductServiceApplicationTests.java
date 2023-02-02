package com.natslash.productservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natslash.productservice.dto.ProductRequest;
import com.natslash.productservice.model.Product;
import com.natslash.productservice.repository.ProductRepository;
import com.natslash.productservice.service.ProductService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductService productService;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void createProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestAsJsonString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestAsJsonString))
				.andExpect(status().isCreated());
	}

	@Test
	void shouldGetProducts() throws Exception {
		Product product1 = Product.builder()
		        .name("Product1")
		        .description("Prodcut1 Description")
				.price(BigDecimal.valueOf(2000))
				.build();

		Product product2 = Product.builder()
				.name("Test2")
				.description("Test2 product")
				.price(BigDecimal.valueOf(4000))
				.build();

		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);

		Mockito.when(productRepository.findAll()).thenReturn(products);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Assertions.assertEquals(2, productRepository.findAll().size(), "Product is not added");
	}

	@Test
	void createProductBadRequest() throws Exception {

		ProductRequest productRequest = getProductRequest();
		String productRequestAsJsonString = objectMapper.writeValueAsString(productRequest);
		String BadRequestString = productRequestAsJsonString.substring(0, productRequestAsJsonString.indexOf("XGIMI"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(BadRequestString))
				.andExpect(status().isBadRequest());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("XGIMI")
				.description("4K Projector")
				.price(BigDecimal.valueOf(1800))
				.build();
	}
}
