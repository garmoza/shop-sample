package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationDtoMapper {

    public Organization toEntity(OrganizationDto.Create organizationDto) {
        return new Organization(
                organizationDto.name(),
                organizationDto.description(),
                organizationDto.logo()
        );
    }

    public OrganizationDto.Default toDtoDefault(Organization organization) {
        return new OrganizationDto.Default(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                organization.getLogo(),
                organization.getBalance(),
                organization.getStatus(),
                organization.getCreationTime()
        );
    }
}
