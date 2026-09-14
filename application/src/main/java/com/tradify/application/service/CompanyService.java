package com.tradify.application.service;

import com.tradify.application.dto.AddUsersToCompanyDto;
import com.tradify.application.dto.CompanyProfileDto;
import com.tradify.application.dto.ProductDto;
import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.Product;
import com.tradify.application.entity.Sector;
import com.tradify.application.entity.User;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.mappers.ProductMapper;
import com.tradify.application.repository.CompanyProfileRepository;
import com.tradify.application.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyProfileRepository companyProfileRepository;
    private final SectorService sectorService;
    private final UserService userService;
    private final ProductRepository productRepository;

    //    TODO: idea - we can launch the company via the agents. We can force it to search for particular items within the app,
    //    set the price, particular parameters, send messages, inquires, emails, etc

    @Transactional
    public void save(CompanyProfileDto dto, String username) throws ObjectNotFoundException {
        Sector sector = sectorService.findById(dto.sector());
        User user = userService.findByUsername(username);
        CompanyProfile companyProfile = new CompanyProfile();
        companyProfile.setUsers(new HashSet<>(Set.of(user)));
        CompanyProfile savedCompany = saveCompany(companyProfile, sector, dto);
        user.setCompanyProfile(savedCompany);

        userService.save(user);
    }

    @Transactional
    public void addUsers(AddUsersToCompanyDto dto){
        CompanyProfile companyProfile = companyProfileRepository.findById(dto.companyId()).orElseThrow(() -> new ObjectNotFoundException("Could not find company by id " + dto.companyId()));
        Set<User> users = userService.findAllUsersByUsernameIn(dto.usernames());
        if (users.isEmpty())
            throw new ObjectNotFoundException("Could not find any users for " + dto.usernames());
        for (User user : users) {
            user.setCompanyProfile(companyProfile);
        }
        userService.saveAll(users);
    }

    public void update(long id, CompanyProfileDto dto) throws ObjectNotFoundException {
        CompanyProfile companyProfile = companyProfileRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found for id " + id));
        Sector sector = sectorService.findById(dto.sector());

        saveCompany(companyProfile, sector, dto);
    }

    private CompanyProfile saveCompany(CompanyProfile companyProfile, Sector sector, CompanyProfileDto dto) {
        companyProfile.setName(dto.name());
        companyProfile.setBuyer(dto.isConsumer());
        companyProfile.setSupplier(dto.isSupplier());
        companyProfile.setLogistics(dto.isLogistics());
        companyProfile.setDescription(dto.description());
        companyProfile.setSector(sector);
        return companyProfileRepository.save(companyProfile);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getCompanyProducts(Long companyId, int pageNumber, int pageSize) {
        Pageable pageReq = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Product> entityPage = productRepository.findBySupplierId(companyId, pageReq);
        return entityPage.map(ProductMapper::toDto);
    }

    @Transactional
    public void saveDefaultBuyer(String username) {
        User user = userService.findByUsername(username);
        CompanyProfile companyProfile = new CompanyProfile();
        companyProfile.setDescription("Default Buyer");
        companyProfile.setName("Default Buyer");
        companyProfile.setBuyer(true);
        companyProfile.setSupplier(false);
        companyProfile.setLogistics(false);
        companyProfile.setSector(sectorService.findDefaultBuyerSector());
        companyProfile.setUsers(new HashSet<>(Set.of(user)));

        companyProfileRepository.save(companyProfile);

        user.setCompanyProfile(companyProfile);
        userService.save(user);
    }
}
