package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.garmoza.shopsample.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private UserRepository userRepository;

    private Organization org;

    @BeforeEach
    void setUp() {
        org = new Organization("name", "description", "logo");
    }

    @Test
    void save() {
        Organization savedOrg = orgRepository.save(org);

        assertThat(savedOrg).isNotNull();
        assertThat(savedOrg.getId()).isPositive();
    }

    @Test
    void save_generatesId() {
        Organization org2 = new Organization("name2", "description2", "logo2");

        orgRepository.save(org);
        orgRepository.save(org2);

        assertEquals(org.getId() + 1, org2.getId());
    }

    @Test
    void save_generatesCreationTime() {
        orgRepository.save(org);

        assertNotNull(org.getCreationTime());
    }

    @Test
    void findAll() {
        Organization org2 = new Organization("name2", "description2", "logo2");

        orgRepository.save(org);
        orgRepository.save(org2);

        List<Organization> orgList = orgRepository.findAll();

        assertThat(orgList)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    void findById() {
        orgRepository.save(org);

        Optional<Organization> optionalOrg = orgRepository.findById(org.getId());

        assertThat(optionalOrg).isPresent();
    }

    @Test
    void findById_organizationNotExist() {
        orgRepository.save(org);

        Optional<Organization> optionalOrg = orgRepository.findById(org.getId() + 1);

        assertThat(optionalOrg).isNotPresent();
    }

    @Test
    void save_update() {
        orgRepository.save(org);

        Optional<Organization> optionalOrg = orgRepository.findById(org.getId());
        assertThat(optionalOrg).isPresent();
        Organization savedOrg = optionalOrg.get();
        savedOrg.setName("new name");
        savedOrg.setDescription("new desc");
        savedOrg.setLogo("new logo");
        savedOrg.setBalance(new BigDecimal("4.75"));
        savedOrg.setStatus(Organization.Status.FROZEN);
        savedOrg.setCreationTime(new Date(12345));

        Organization updatedOrg = orgRepository.save(savedOrg);

        assertThat(updatedOrg)
                .usingRecursiveComparison()
                .isEqualTo(savedOrg);
    }

    @Test
    void deleteById() {
        orgRepository.save(org);

        orgRepository.deleteById(org.getId());
        Optional<Organization> optionalOrg = orgRepository.findById(org.getId());

        assertThat(optionalOrg).isNotPresent();
    }

    @Test
    void deleteById_organizationNotExist() {
        orgRepository.save(org);

        orgRepository.deleteById(org.getId() + 1);
        Optional<Organization> optionalOrg = orgRepository.findById(org.getId() + 1);

        assertThat(optionalOrg).isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_name_notBlank(String name) {
        org.setName(name);

        var e = assertThrows(ConstraintViolationException.class, () -> orgRepository.save(org));
        assertNotBlankAnnotation(e.getConstraintViolations(), "name");
    }

    @Test
    void validation_description_notNull() {
        org.setDescription(null);

        var e = assertThrows(ConstraintViolationException.class, () -> orgRepository.save(org));
        assertNotNullAnnotation(e.getConstraintViolations(), "description");
    }

    @Test
    void validation_balance_notNull() {
        org.setBalance(null);

        var e = assertThrows(ConstraintViolationException.class, () -> orgRepository.save(org));
        assertNotNullAnnotation(e.getConstraintViolations(), "balance");
    }

    @Test
    void validation_status_notNull() {
        org.setStatus(null);

        var e = assertThrows(ConstraintViolationException.class, () -> orgRepository.save(org));
        assertNotNullAnnotation(e.getConstraintViolations(), "status");
    }

    // todo: delete not working? how clear db? testing on prod?
    @Test
    @DisplayName("remove an organization's association with a user after deleting user")
    void user_onDeleteUserAction_removes() {
        // given
        User user = new User("username", "test@mail.com", "pass");
        user.setAuthorities(Set.of("ROLE_CUSTOMER"));
        org.setUser(user);

        commit(() -> {
            userRepository.save(user);
            orgRepository.save(org);
        });

        Optional<User> optionalUser = userRepository.findById(user.getId());
        assertThat(optionalUser).isPresent();
        Optional<Organization> optionalOrg = orgRepository.findById(org.getId());
        assertThat(optionalOrg).isPresent();
        assertThat(optionalOrg.get().getUser()).isNotNull();

        // when
        commit(() -> {
            userRepository.deleteById(user.getId());
        });
        optionalUser = userRepository.findById(user.getId());
        assertThat(optionalUser).isNotPresent();

        // then
        optionalOrg = orgRepository.findById(org.getId());
        assertThat(optionalOrg).isPresent();
        assertThat(optionalOrg.get().getUser()).isNull();

        // clear
        commit(() -> {
            userRepository.deleteById(user.getId());
            orgRepository.deleteById(org.getId());
        });
    }
}