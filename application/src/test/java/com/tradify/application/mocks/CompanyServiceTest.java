package com.tradify.application.mocks;

import com.tradify.application.dto.CompanyProfileDto;
import com.tradify.application.dto.SectorDto;
import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.Sector;
import com.tradify.application.entity.User;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.repository.CompanyProfileRepository;
import com.tradify.application.service.CompanyService;
import com.tradify.application.service.SectorService;
import com.tradify.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyProfileRepository companyProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private SectorService sectorService;

    @InjectMocks
    private CompanyService companyService;

    private Sector createSector(String name, String description) {
        return sectorService.save(new SectorDto(name, description));
    }

    @Test
    void shouldCreateCompanyAndAssignToUser() throws ObjectNotFoundException {
        String username = "test-user-1";

        Sector fakeSector = new Sector();

        User fakeUser = new User();
        fakeUser.setUsername(username);

        CompanyProfileDto dto = new CompanyProfileDto("Global Logistics", "Desc", 1L, true, false, false);

        CompanyProfile fakeSavedCompany = new CompanyProfile();
        fakeSavedCompany.setId(100L);

        when(sectorService.findById(1L)).thenReturn(fakeSector);
        when(userService.findByUsername(username)).thenReturn(fakeUser);
        when(companyProfileRepository.save(any(CompanyProfile.class))).thenReturn(fakeSavedCompany);

        companyService.save(dto, username);

        verify(companyProfileRepository, times(1)).save(any(CompanyProfile.class));

        assertEquals(100L, fakeUser.getCompanyProfile().getId());
        verify(userService, times(1)).save(fakeUser);
    }

    @Test
    void shouldAddCompaniesToSector() throws ObjectNotFoundException {
        String username = "test-user-1";

        Sector fakeSector = new Sector();
        fakeSector.setId(1L);
        fakeSector.setName("Logistics");

        User fakeUser = new User();
        fakeUser.setUsername(username);

        CompanyProfileDto dto1 =
                new CompanyProfileDto("Global Logistics 1", "Desc", 1L, true, false, false);

        CompanyProfileDto dto2 =
                new CompanyProfileDto("Global Logistics 2", "Desc", 1L, true, false, false);

        CompanyProfileDto dto3 =
                new CompanyProfileDto("Global Logistics 3", "Desc", 1L, true, false, false);

        when(sectorService.findById(1L))
                .thenReturn(fakeSector);

        when(userService.findByUsername(username))
                .thenReturn(fakeUser);

        when(companyProfileRepository.save(any(CompanyProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        companyService.save(dto1, username);
        companyService.save(dto2, username);
        companyService.save(dto3, username);

        ArgumentCaptor<CompanyProfile> captor =
                ArgumentCaptor.forClass(CompanyProfile.class);

        verify(companyProfileRepository, times(3))
                .save(captor.capture());

        List<CompanyProfile> savedCompanies = captor.getAllValues();

        assertEquals(3, savedCompanies.size());

        assertSame(fakeSector, savedCompanies.get(0).getSector());
        assertSame(fakeSector, savedCompanies.get(1).getSector());
        assertSame(fakeSector, savedCompanies.get(2).getSector());
    }
}
