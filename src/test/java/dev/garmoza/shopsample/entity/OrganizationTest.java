package dev.garmoza.shopsample.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    private Organization organization;

    @BeforeEach
    void setUp() {
        organization = new Organization("name", "description", "logo");
    }

    @Test
    void createOrganization() {
        Organization organization1 = new Organization("name", "description", "logo");

        assertNull(organization1.getId());
        assertEquals("name", organization1.getName());
        assertEquals("description", organization1.getDescription());
        assertEquals("logo", organization1.getLogo());
        assertEquals(BigDecimal.ZERO, organization1.getBalance());
        assertEquals(Organization.Status.CONFIRMATION, organization1.getStatus());
        assertNull(organization1.getCreationTime());
        assertNull(organization1.getUser());
    }

    @Test
    void createOrganizationWithBalance() {
        Organization organization1 = new Organization("name", "description", "logo", new BigDecimal("200.45"));

        assertNull(organization1.getId());
        assertEquals("name", organization1.getName());
        assertEquals("description", organization1.getDescription());
        assertEquals("logo", organization1.getLogo());
        assertEquals(new BigDecimal("200.45"), organization1.getBalance());
        assertEquals(Organization.Status.CONFIRMATION, organization1.getStatus());
        assertNull(organization1.getCreationTime());
        assertNull(organization1.getUser());
    }

    @Test
    void createOrganizationWithBalanceAndStatus() {
        Organization organization1 = new Organization("name", "description", "logo", new BigDecimal("200.45"), Organization.Status.ENABLED);

        assertNull(organization1.getId());
        assertEquals("name", organization1.getName());
        assertEquals("description", organization1.getDescription());
        assertEquals("logo", organization1.getLogo());
        assertEquals(new BigDecimal("200.45"), organization1.getBalance());
        assertEquals(Organization.Status.ENABLED, organization1.getStatus());
        assertNull(organization1.getCreationTime());
        assertNull(organization1.getUser());
    }

    @Test
    void setId() {
        organization.setId(2L);

        assertEquals(2L, organization.getId());
    }

    @Test
    void setName() {
        organization.setName("new name");

        assertEquals("new name", organization.getName());
    }

    @Test
    void setDescription() {
        organization.setDescription("new desc");

        assertEquals("new desc", organization.getDescription());
    }

    @Test
    void setLogo() {
        organization.setLogo("new logo");

        assertEquals("new logo", organization.getLogo());
    }

    @Test
    void setBalance() {
        organization.setBalance(new BigDecimal("4.75"));

        assertEquals(new BigDecimal("4.75"), organization.getBalance());
    }

    @Test
    void setStatus() {
        organization.setStatus(Organization.Status.FROZEN);

        assertEquals(Organization.Status.FROZEN, organization.getStatus());
    }

    @Test
    void setCreationTime() {
        organization.setCreationTime(new Date(12345));

        assertEquals(new Date(12345), organization.getCreationTime());
    }

    @Test
    void setUser() {
        User user = new User("username", "test@mail.com", "pass");
        organization.setUser(user);

        assertEquals(user, organization.getUser());
    }
}