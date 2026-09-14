package com.tradify.application.controller;

import com.tradify.application.dto.SectorDto;
import com.tradify.application.repository.SectorRepository;
import com.tradify.application.service.SectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sector")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping
    public ResponseEntity<List<SectorDto>> getAllSectors() {
        return ResponseEntity.ok(sectorService.getAllSectors());
    }
}
