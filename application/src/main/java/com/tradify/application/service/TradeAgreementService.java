package com.tradify.application.service;


import com.tradify.application.repository.TradeAgreementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TradeAgreementService {

    private final TradeAgreementRepository tradeAgreementRepository;


}
