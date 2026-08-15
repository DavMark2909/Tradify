package com.tradify.application.repository;

import com.tradify.application.entity.TradeAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeAgreementRepository extends JpaRepository<TradeAgreement, Long> {
}
