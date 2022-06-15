package com.hcl.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "album_genre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "genre")
    private String genre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genre")
    private Set<Product> products;

    public ProductCategory(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }
}
