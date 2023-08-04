package dev.garmoza.shopsample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.garmoza.shopsample.dto.OrganizationDto;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.exception.OrganizationNotFoundException;
import dev.garmoza.shopsample.exception.UserNotFoundException;
import dev.garmoza.shopsample.service.OrganizationService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService orgService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrganizationDto.Create orgDtoCreate;
    private OrganizationDto.Default orgDtoDefault;

    @BeforeEach
    void setUp() {
        orgDtoCreate = new OrganizationDto.Create(
                1L,
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
    void getAllOrganizations() throws Exception {
        List<OrganizationDto.Default> responseDto = List.of(orgDtoDefault);
        given(orgService.findAllOrganizations()).willReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(responseDto.size())));
    }

    @Test
    void postOrganization() throws Exception {
        given(orgService.createOrganization(orgDtoCreate)).willReturn(orgDtoDefault);

        ResultActions response = mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "name": "name",
                            "description": "description",
                            "logo": "logo",
                            "balance": 0,
                            "status": "CONFIRMATION",
                            "creationTime": %s
                        }
                        """.formatted(objectMapper.writeValueAsString(orgDtoDefault.creationTime()))
                ));
    }

    @Test
    void postOrganization_userNotFound() throws Exception {
        given(orgService.createOrganization(orgDtoCreate)).willThrow(new UserNotFoundException(orgDtoCreate.userId()));

        ResultActions response = mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void postOrganization_validation() throws Exception {
        OrganizationDto.Create notValid = new OrganizationDto.Create(
                -2L,
                " ",
                null,
                "logo"
        );

        ResultActions response = mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValid))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void putOrganization() throws Exception {
        OrganizationDto.Default dtoForUpdate = new OrganizationDto.Default(
                1L,
                "new name",
                "new description",
                "new logo",
                new BigDecimal("3.33"),
                Organization.Status.FROZEN,
                new Date(54321)
        );
        given(orgService.updateOrganization(dtoForUpdate)).willReturn(dtoForUpdate);

        ResultActions response = mockMvc.perform(put("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoForUpdate))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "name": "new name",
                            "description": "new description",
                            "logo": "new logo",
                            "balance": 3.33,
                            "status": "FROZEN",
                            "creationTime": %s
                        }
                        """.formatted(objectMapper.writeValueAsString(dtoForUpdate.creationTime()))
                ));
    }

    @Test
    void putOrganization_organizationNotFound() throws Exception {
        given(orgService.updateOrganization(orgDtoDefault)).willThrow(new OrganizationNotFoundException(orgDtoDefault.id()));

        ResultActions response = mockMvc.perform(put("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orgDtoDefault))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void putOrganization_validation() throws Exception {
        OrganizationDto.Default notValid = new OrganizationDto.Default(
                -2L,
                " ",
                null,
                "logo",
                null,
                null,
                new Date(12345)
        );

        ResultActions response = mockMvc.perform(put("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValid))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getOrganizationById() throws Exception {
        Long orgId = 1L;
        given(orgService.findOrganizationById(orgId)).willReturn(orgDtoDefault);

        ResultActions response = mockMvc.perform(get("/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "name": "name",
                            "description": "description",
                            "logo": "logo",
                            "balance": 0,
                            "status": "CONFIRMATION",
                            "creationTime": %s
                        }
                        """.formatted(objectMapper.writeValueAsString(orgDtoDefault.creationTime()))
                ));
    }

    @Test
    void getOrganizationById_organizationNotFound() throws Exception {
        Long orgId = 1L;
        given(orgService.findOrganizationById(orgId)).willThrow(new OrganizationNotFoundException(orgId));

        ResultActions response = mockMvc.perform(get("/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -2L})
    void getOrganizationById_validation(long orgId) throws Exception {
        ResultActions response = mockMvc.perform(get("/organizations/%d".formatted(orgId))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}