package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.OrganizationDto;
import dev.garmoza.shopsample.dto.OrganizationDtoMapper;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.User;
import dev.garmoza.shopsample.exception.OrganizationNotFoundException;
import dev.garmoza.shopsample.repository.OrganizationRepository;
import dev.garmoza.shopsample.repository.UserRepository;
import dev.garmoza.shopsample.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository orgRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationDtoMapper orgDtoMapper;

    @InjectMocks
    private OrganizationServiceImpl orgService;

    @Captor
    private ArgumentCaptor<Organization> orgArgumentCaptor;

    private User user;
    private Organization org;
    private OrganizationDto.Create orgDtoCreate;
    private OrganizationDto.Default orgDtoDefault;

    @BeforeEach
    void setUp() {
        user = new User("username", "test@mail.com", "pass");
        user.setId(2L);
        org = new Organization("name", "description", "logo");

        orgDtoCreate = new OrganizationDto.Create(
                2L,
                "name",
                "description",
                "logo"
        );
        orgDtoDefault = new OrganizationDto.Default(
                1L,
                "name",
                "description",
                "logo",
                BigDecimal.ZERO,
                Organization.Status.CONFIRMATION,
                new Date(12345)
        );
    }

    @Test
    void saveNewOrganization() {
        given(orgRepository.save(Mockito.any(Organization.class))).will(i -> i.getArgument(0));

        Organization savedOrg = orgService.saveNewOrganization(user, org);

        then(orgRepository).should().save(orgArgumentCaptor.capture());
        Organization value = orgArgumentCaptor.getValue();
        assertThat(value).isEqualTo(savedOrg);
        assertThat(value.getUser()).isEqualTo(user);
    }

    @Test
    void createOrganization() {
        given(orgDtoMapper.toEntity(orgDtoCreate)).willReturn(org);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(orgRepository.save(org)).willReturn(org);
        given(orgDtoMapper.toDtoDefault(org)).willReturn(orgDtoDefault);

        OrganizationDto.Default dto = orgService.createOrganization(orgDtoCreate);

        then(orgRepository).should().save(org);
        assertThat(dto).isEqualTo(orgDtoDefault);
    }

    @Test
    void createOrganization_userNotFound() {
        // todo
    }

    @Test
    void findAllOrganizations() {
        List<Organization> orgList = List.of(org, org, org);
        given(orgRepository.findAll()).willReturn(orgList);
        given(orgDtoMapper.toDtoDefault(Mockito.any(Organization.class))).willReturn(orgDtoDefault);

        List<OrganizationDto.Default> dtoList = orgService.findAllOrganizations();

        then(orgRepository).should().findAll();
        then(orgDtoMapper).should(times(3)).toDtoDefault(org);
        assertThat(dtoList).hasSize(3);
    }

    @Test
    void findOrganizationById() {
        Long orgId = 2L;
        org.setId(orgId);
        given(orgRepository.findById(orgId)).willReturn(Optional.of(org));
        given(orgDtoMapper.toDtoDefault(org)).willReturn(orgDtoDefault);

        OrganizationDto.Default dto = orgService.findOrganizationById(orgId);

        then(orgRepository).should().findById(orgId);
        then(orgDtoMapper).should().toDtoDefault(org);

        assertThat(dto).isEqualTo(orgDtoDefault);
    }

    @Test
    void findOrganizationById_organizationNotFound() {
        Long orgId = 2L;
        given(orgRepository.findById(orgId)).willReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class, () -> orgService.findOrganizationById(orgId));
    }

    @Test
    void updateOrganization() {
        org.setCreationTime(new Date(54321));
        OrganizationDto.Default dtoForUpdate = new OrganizationDto.Default(
                org.getId(),
                "new name",
                "new desc",
                "new logo",
                new BigDecimal("10.2"),
                Organization.Status.FROZEN,
                new Date(54321)
        );

        given(orgRepository.findById(org.getId())).willReturn(Optional.of(org));
        given(orgRepository.save(Mockito.any(Organization.class))).willReturn(org);
        given(orgDtoMapper.toDtoDefault(org)).willReturn(orgDtoDefault);

        OrganizationDto.Default updatedDto = orgService.updateOrganization(dtoForUpdate);

        // updated instance before save
        then(orgRepository).should().save(orgArgumentCaptor.capture());
        Organization value = orgArgumentCaptor.getValue();
        assertEquals(dtoForUpdate.id(), value.getId());
        assertEquals(dtoForUpdate.name(), value.getName());
        assertEquals(dtoForUpdate.description(), value.getDescription());
        assertEquals(dtoForUpdate.logo(), value.getLogo());
        assertEquals(dtoForUpdate.balance(), value.getBalance());
        assertEquals(dtoForUpdate.status(), value.getStatus());
        assertEquals(dtoForUpdate.creationTime(), value.getCreationTime());

        assertThat(updatedDto).isEqualTo(orgDtoDefault);
    }

    @Test
    void updateOrganization_organizationNotFound() {
        given(orgRepository.findById(orgDtoDefault.id())).willReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class, () -> orgService.updateOrganization(orgDtoDefault));
    }
}