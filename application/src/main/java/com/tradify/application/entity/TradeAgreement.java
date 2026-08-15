package com.tradify.application.entity;

import com.tradify.application.entity.enums.TradeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade_agreements")
@Getter
@Setter
public class TradeAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_profile_id", nullable = false)
    private CompanyProfile buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_profile_id", nullable = false)
    private CompanyProfile seller;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "purchase_price", nullable = false)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private String currency; // e.g., "USD"

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_of_measure", nullable = false)
    private String unitOfMeasure; // e.g., "Metric Ton"
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TradeStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

