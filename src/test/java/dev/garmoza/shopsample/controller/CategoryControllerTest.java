package dev.garmoza.shopsample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.garmoza.shopsample.dto.CategoryDto;
import dev.garmoza.shopsample.exception.CategoryNotFoundException;
import dev.garmoza.shopsample.service.CategoryService;
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

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDto.Create categoryDtoCreate;
    private CategoryDto.Default categoryDtoDefault;
    private CategoryDto.Full categoryDtoFull;

    @BeforeEach
    void setUp() {
        categoryDtoCreate = new CategoryDto.Create("category name");
        categoryDtoDefault = new CategoryDto.Default(1L, "category name");
        categoryDtoFull = new CategoryDto.Full(1L, "category name", List.of(
                new CategoryDto.Full(2L, "sub category name", List.of())
        ));
    }

    @Test
    void getAllCategories() throws Exception {
        List<CategoryDto.Full> responseDto = List.of(categoryDtoFull);
        given(categoryService.findAllCategories()).willReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(responseDto.size())))
                .andExpect(content().json(
                        """
                        [
                          {
                            "id": 1,
                            "name": "category name",
                            "children": [
                              {
                                "id": 2,
                                "name": "sub category name",
                                "children": []
                              }
                            ]
                          }
                        ]
                        """
                ));
    }

    @Test
    void postCategory() throws Exception {
        given(categoryService.createCategory(categoryDtoCreate)).willReturn(categoryDtoDefault);

        ResultActions response = mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "name": "category name"
                        }
                        """
                ));
    }

    @Test
    void postCategory_validation() throws Exception {
        CategoryDto.Create notValid = new CategoryDto.Create(
                " "
        );

        ResultActions response = mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValid))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void postCategoryByParentId() throws Exception {
        given(categoryService.createCategory(2L, categoryDtoCreate)).willReturn(categoryDtoDefault);

        ResultActions response = mockMvc.perform(post("/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "name": "category name"
                        }
                        """
                ));
    }

    @Test
    void postCategoryByParentId_categoryNotFound() throws Exception {
        given(categoryService.createCategory(2L, categoryDtoCreate)).willThrow(new CategoryNotFoundException(2L));

        ResultActions response = mockMvc.perform(post("/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void postCategoryByParentId_validation_requestBody() throws Exception {
        CategoryDto.Create notValid = new CategoryDto.Create(
                " "
        );

        ResultActions response = mockMvc.perform(post("/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValid))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -2L})
    void postCategoryByParentId_validation_pathVariable(long parentCategoryId) throws Exception {
        ResultActions response = mockMvc.perform(post("/categories/%d".formatted(parentCategoryId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}