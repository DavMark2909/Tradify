package com.tradify.application.service;

import com.tradify.application.dto.ProductCreateDto;
import com.tradify.application.dto.ProductDto;
import com.tradify.application.entity.Product;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.kafka.dto.ProductEvent;
import com.tradify.application.kafka.dto.ProductPayload;
import com.tradify.application.kafka.producers.ProductEventProducer;
import com.tradify.application.mappers.ProductMapper;
import com.tradify.application.repository.CompanyProfileRepository;
import com.tradify.application.repository.ProductRepository;
import com.tradify.application.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SectorRepository sectorRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final ProductEventProducer productEventProducer;

    public List<ProductDto> getTrendingProducts() {
        return productRepository.findTop10ByOrderByCreatedAtDesc().stream().map(ProductMapper::toDto).toList();
    }

    @Transactional
    public void createProduct(ProductCreateDto dto) {
        Product product = new Product();
        setProductDetails(product, dto);

        product.setSector(sectorRepository.getReferenceById(dto.sectorId()));
        product.setSupplier(companyProfileRepository.getReferenceById(dto.supplierCompanyId()));

        Product savedProduct = productRepository.save(product);
        ProductPayload payload = setProductPayload(savedProduct);

        productEventProducer.publishProductCreated(ProductEvent.created(payload));
    }

    @Transactional
    public void updateProduct(ProductCreateDto dto, long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
        setProductDetails(product, dto);

        Product savedProduct = productRepository.save(product);
        ProductPayload payload = setProductPayload(savedProduct);

        productEventProducer.publishProductCreated(ProductEvent.updated(payload));
    }

    private void setProductDetails(Product product, ProductCreateDto dto){
        product.setTitle(dto.title());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCurrency(dto.currency());
        product.setUnitOfMeasure(dto.unitOfMeasure());
        product.setAvailableQuantity(dto.availableQuantity());
        product.setStatus(dto.status());
    }

    private ProductPayload setProductPayload(Product savedProduct){
        return new ProductPayload(
                savedProduct.getId(),
                savedProduct.getSupplier().getId(),
                savedProduct.getSector().getId(),
                savedProduct.getTitle(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getCurrency(),
                savedProduct.getUnitOfMeasure(),
                savedProduct.getAvailableQuantity(),
                savedProduct.getStatus()
        );
    }



}
