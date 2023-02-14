package com.natslash.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.natslash.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
