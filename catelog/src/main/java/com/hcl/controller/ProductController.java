package com.hcl.controller;

import com.hcl.entity.Product;
import com.hcl.entity.ProductCategory;
import com.hcl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("${allowed.origins}")
@RestController
@RequestMapping("/api/dashboard")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/{category_id}/products")
    public Product addProduct(@RequestBody Product product, @PathVariable Long category_id) {
        product.setGenre(new ProductCategory(category_id, ""));
        return productService.addProduct(product);
    }

    @PutMapping("/{category_id}/products/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable Long id,  @PathVariable Long category_id) {
        product.setGenre(new ProductCategory(category_id, ""));
        return productService.updateProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
