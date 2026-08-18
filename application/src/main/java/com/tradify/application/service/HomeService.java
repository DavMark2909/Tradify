package com.tradify.application.service;

import com.tradify.application.dto.ProductDto;
import com.tradify.application.dto.SavedItemDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeService {

    private final ProductService productService;
    private final SavedItemService savedItemService;
    private final UserService userService;
    private final TradeAgreementService tradeAgreementService;

    public List<ProductDto> getTrendingProducts() {
        return productService.getTrendingProducts();
    }

    public Page<SavedItemDto> getSavedItems(Long userId, int pageNumber, int pageSize) {
        return savedItemService.getSavedItems(userId, pageNumber, pageSize);
    }
}
