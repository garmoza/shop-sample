package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.OrganizationDto;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.User;

import java.util.List;

public interface OrganizationService {
    Organization saveNewOrganization(User user, Organization organization);
    OrganizationDto.Default createOrganization(OrganizationDto.Create organizationDto);
    List<OrganizationDto.Default> findAllOrganizations();
    OrganizationDto.Default findOrganizationById(Long id);
    OrganizationDto.Default updateOrganization(OrganizationDto.Default organizationDto);
}
