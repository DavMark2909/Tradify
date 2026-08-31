package com.tradify.application.controller;

import com.tradify.application.dto.AddUsersToCompanyDto;
import com.tradify.application.dto.CompanyProfileDto;
import com.tradify.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
}
