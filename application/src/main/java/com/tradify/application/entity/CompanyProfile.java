package com.tradify.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "company_profiles")
@Data
@NoArgsConstructor
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "is_buyer", nullable = false)
    private boolean isBuyer = false;

    @Column(name = "is_supplier", nullable = false)
    private boolean isSupplier = false;

    @Column(name = "is_logistics", nullable = false)
    private boolean isLogistics = false;

//    maps by the variable name in user
    @OneToMany(mappedBy = "companyProfile")
    private Set<User> user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id")
    private Sector sector;
}
