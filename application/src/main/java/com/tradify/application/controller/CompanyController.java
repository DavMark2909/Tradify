package com.tradify.application.controller;

import com.tradify.application.dto.AddUsersToCompanyDto;
import com.tradify.application.dto.CompanyProfileDto;
import com.tradify.application.dto.ProductDto;
import com.tradify.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<Void> createCompany(@RequestBody CompanyProfileDto dto, JwtAuthenticationToken token){
        String username = token.getName();
        companyService.save(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create-default-buyer")
    public ResponseEntity<Void> createBuyer(JwtAuthenticationToken token){
        String username = token.getName();
        companyService.saveDefaultBuyer(username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add-users")
    public ResponseEntity<Void> addUsers(@RequestBody AddUsersToCompanyDto dto){
        companyService.addUsers(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateCompany(@RequestBody CompanyProfileDto dto, @RequestParam long id){
        companyService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-product")
    public ResponseEntity<Page<ProductDto>> getAllProduct(@RequestParam long id,
                                                          @RequestParam(defaultValue = "20") int page,
                                                          @RequestParam(defaultValue = "0") int size){
        return ResponseEntity.ok(companyService.getCompanyProducts(id, page, size));
    }
}
