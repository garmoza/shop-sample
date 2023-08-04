package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.OrganizationDto;
import dev.garmoza.shopsample.service.OrganizationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/organizations")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<OrganizationDto.Default> getAllOrganizations() {
        return organizationService.findAllOrganizations();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationDto.Default postOrganization(@Valid @RequestBody OrganizationDto.Create organizationDto) {
        return organizationService.createOrganization(organizationDto);
    }

    @PutMapping
    public OrganizationDto.Default putOrganization(@Valid @RequestBody OrganizationDto.Default organizationDto) {
        return organizationService.updateOrganization(organizationDto);
    }

    @GetMapping("/{id}")
    public OrganizationDto.Default getOrganizationById(@PathVariable @Positive Long id) {
        return organizationService.findOrganizationById(id);
    }
}
