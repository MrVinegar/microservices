package com.hcl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    private Long id;
    private String genre;
    private Set<Product> products;

    public ProductCategory(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }
}
