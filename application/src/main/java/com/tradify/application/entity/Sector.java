package com.tradify.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "sectors")
@Data
@NoArgsConstructor
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "sector")
    private Set<CompanyProfile> companies;

    @OneToMany(mappedBy = "sector")
    private Set<Product> products;

}
