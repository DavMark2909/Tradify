package com.tradify.application.service;

import com.tradify.application.entity.Sector;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;

    public Sector findById(long id) throws ObjectNotFoundException {
        return sectorRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Sector not found for id " + id));
    }
}
