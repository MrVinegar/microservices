package com.hcl.service;

import com.hcl.repo.ProductRepository;
import com.hcl.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public Product addProduct(Product product){
        productRepository.save(product);
        return product;
    }

    public Product updateProduct(Product product){
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Long id) {productRepository.deleteById(id);}
}
