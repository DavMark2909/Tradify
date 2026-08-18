package com.tradify.application.mappers;

import com.tradify.application.dto.ProductDto;
import com.tradify.application.entity.Product;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        String companyName = (product.getSupplier() != null && product.getSupplier().getName() != null)
                ? product.getSupplier().getName()
                : "Unknown Supplier";

        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency(),
                product.getUnitOfMeasure(),
                product.getAvailableQuantity(),
                product.getStatus(),
                companyName,
                product.getCreatedAt()
        );

    }
}
