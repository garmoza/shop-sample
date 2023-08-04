package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationDtoMapperTest {

    private OrganizationDtoMapper orgDtoMapper;

    @BeforeEach
    void setUp() {
        orgDtoMapper = new OrganizationDtoMapper();
    }

    @Test
    void toEntity() {
        OrganizationDto.Create orgDtoCreate = new OrganizationDto.Create(
                1L,
                "name",
                "description",
                "logo"
        );
        Organization org = new Organization("name", "description", "logo");

        Organization orgEntity = orgDtoMapper.toEntity(orgDtoCreate);

        assertThat(orgEntity)
                .usingRecursiveComparison()
                .isEqualTo(org);
    }

    @Test
    void toDtoDefault() {
        Organization org = new Organization("name", "description", "logo");
        org.setId(1L);
        org.setBalance(new BigDecimal("1.25"));
        org.setStatus(Organization.Status.FROZEN);
        org.setCreationTime(new Date(12345));

        OrganizationDto.Default dto = orgDtoMapper.toDtoDefault(org);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(new OrganizationDto.Default(
                        1L,
                        "name",
                        "description",
                        "logo",
                        new BigDecimal("1.25"),
                        Organization.Status.FROZEN,
                        new Date(12345)
                ));
    }
}