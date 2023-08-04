package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.OrganizationDto;
import dev.garmoza.shopsample.dto.OrganizationDtoMapper;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.User;
import dev.garmoza.shopsample.exception.OrganizationNotFoundException;
import dev.garmoza.shopsample.exception.UserNotFoundException;
import dev.garmoza.shopsample.repository.OrganizationRepository;
import dev.garmoza.shopsample.repository.UserRepository;
import dev.garmoza.shopsample.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationDtoMapper dtoMapper;

    @Override
    public Organization saveNewOrganization(User user, Organization organization) {
        organization.setUser(user);
        return organizationRepository.save(organization);
    }

    @Override
    public OrganizationDto.Default createOrganization(OrganizationDto.Create organizationDto) {
        User user = userRepository.findById(organizationDto.userId())
                .orElseThrow(() -> new UserNotFoundException(organizationDto.userId()));
        Organization newOrganization = dtoMapper.toEntity(organizationDto);

        Organization organization = saveNewOrganization(user, newOrganization);

        return dtoMapper.toDtoDefault(organization);
    }

    @Override
    public List<OrganizationDto.Default> findAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(dtoMapper::toDtoDefault)
                .toList();
    }

    @Override
    public OrganizationDto.Default findOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));

        return dtoMapper.toDtoDefault(organization);
    }

    @Override
    public OrganizationDto.Default updateOrganization(OrganizationDto.Default organizationDto) {
        Organization updatedOrganization = organizationRepository.findById(organizationDto.id())
                .map(org -> {
                    org.setName(organizationDto.name());
                    org.setDescription(organizationDto.description());
                    org.setLogo(organizationDto.logo());
                    org.setBalance(organizationDto.balance());
                    org.setStatus(organizationDto.status());
                    return organizationRepository.save(org);
                })
                .orElseThrow(() -> new OrganizationNotFoundException(organizationDto.id()));

        return dtoMapper.toDtoDefault(updatedOrganization);
    }
}
