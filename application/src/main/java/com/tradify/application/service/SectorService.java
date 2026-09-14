package com.tradify.application.service;

import com.tradify.application.dto.SectorDto;
import com.tradify.application.entity.Sector;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {

    public static final String DEFAULT_BUYER_SECTOR_NAME = "Buyer";

    private final SectorRepository sectorRepository;

    public Sector findById(long id) throws ObjectNotFoundException {
        return sectorRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Sector not found for id " + id));
    }

    public Sector findDefaultBuyerSector() throws ObjectNotFoundException {
        return sectorRepository.findByName(DEFAULT_BUYER_SECTOR_NAME)
                .orElseThrow(() -> new ObjectNotFoundException("Default buyer sector not found"));
    }

    public Sector save(SectorDto sectorDto) {
        Sector sector = new Sector();
        sector.setName(sectorDto.name());
        sector.setDescription(sectorDto.description());
        return sectorRepository.save(sector);
    }

    public List<SectorDto> getAllSectors(){
        return sectorRepository.findAll().stream()
                .map(s -> new SectorDto(s.getId(), s.getName(), s.getDescription()))
                .toList();
    }
}
