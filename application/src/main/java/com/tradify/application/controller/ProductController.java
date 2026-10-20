package com.tradify.application.controller;

import com.tradify.application.dto.ProductUpdateDto;
import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.User;
import com.tradify.application.service.ProductService;
import com.tradify.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ProductUpdateDto product, JwtAuthenticationToken token) {
        User byUsername = userService.findByUsernameWithCompany(token.getName());
        CompanyProfile companyProfile = byUsername.getCompanyProfile();
        productService.createProduct(product, companyProfile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody ProductUpdateDto product, @RequestParam long id) {
        productService.updateProduct(product, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
