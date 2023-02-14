package com.natslash.orderservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natslash.orderservice.dto.OrderLineItemsDto;
import com.natslash.orderservice.dto.OrderRequest;
import com.natslash.orderservice.model.Order;
import com.natslash.orderservice.model.OrderLineItems;
import com.natslash.orderservice.repository.OrderRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

	@Container
	private static final MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:latest")
			.withDatabaseName("test_order_service")
			.withUsername("test_order")
			.withPassword("test_order");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderRepository orderRepository;

	@BeforeAll
	void initDatabaseProperties() {
		System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
		System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
		System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
	}

	@Test
	void shouldCreateOrder() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String orderRequestString = objectMapper.writeValueAsString(orderRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderRequestString))
				.andExpect(status().isCreated());
	}

	@Test
	void shouldGetOrders() throws Exception {
		OrderLineItems orderLineItem1 = OrderLineItems.builder()
				.skuCode("msr123")
				.price(BigDecimal.valueOf(123500))
				.quantity(1)
				.build();

		OrderLineItems orderLineItem2 = OrderLineItems.builder()
				.skuCode("msr123")
				.price(BigDecimal.valueOf(123500))
				.quantity(1)
				.build();

		List<OrderLineItems> orderLineItemsList = new ArrayList<>();
		orderLineItemsList.add(orderLineItem1);
		orderLineItemsList.add(orderLineItem2);

		Order order = Order.builder()
				.orderNumber("123")
				.orderLineItemsList(orderLineItemsList)
				.build();

		List<Order> orders = new ArrayList<>();
		orders.add(order);

		Mockito.when(orderRepository.findAll()).thenReturn(orders);

		String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/order")
				.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

		Assertions.assertTrue(response != null);
	}

	@Test
	void createOrderBadRequest() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String orderRequestString = objectMapper.writeValueAsString(orderRequest);
		String BadRequestString = orderRequestString.substring(0, orderRequestString.indexOf("msr123"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(BadRequestString))
				.andExpect(status().isBadRequest());
	}

	private OrderRequest getOrderRequest() {
		OrderLineItemsDto orderLineItem1 = OrderLineItemsDto.builder()
				.skuCode("msr123")
				.price(BigDecimal.valueOf(123500))
				.quantity(1)
				.build();

		OrderLineItemsDto orderLineItem2 = OrderLineItemsDto.builder()
				.skuCode("msr123")
				.price(BigDecimal.valueOf(123500))
				.quantity(1)
				.build();

		List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<>();
		orderLineItemsDtoList.add(orderLineItem1);
		orderLineItemsDtoList.add(orderLineItem2);

		return new OrderRequest(orderLineItemsDtoList);
	}

}
