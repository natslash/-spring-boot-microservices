package com.natslash.inventoryservice;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natslash.inventoryservice.repository.InventoryRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

	@Container
	private static final MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:latest")
			.withDatabaseName("test_inventory_service")
			.withUsername("test_order")
			.withPassword("test_order");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private InventoryRepository inventoryRepository;

	@BeforeAll
	void initDatabaseProperties() {
		System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
		System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
		System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
	}
}
