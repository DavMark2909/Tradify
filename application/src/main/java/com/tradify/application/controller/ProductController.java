package com.tradify.application.controller;

import com.tradify.application.dto.ProductCreateDto;
import com.tradify.application.entity.Product;
import com.tradify.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ProductCreateDto product) {
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody ProductCreateDto product, @RequestParam long id) {
        productService.updateProduct(product, id);
        return ResponseEntity.ok().build();
    }
}
