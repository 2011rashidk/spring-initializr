package org.happiest.minds.springinitializr.controller;

import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.happiest.minds.springinitializr.service.SpringInitializrService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@WebMvcTest(controllers = SpringInitializrController.class)
class SpringInitializrControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private SpringInitializrController springInitializrController;


    @Mock
    private SpringInitializrService springInitializrService;


    @Test
    void testDownloadTemplateWithValidationErrorsPositive() {
        SpringInitializrRequest request = new SpringInitializrRequest();
        request.setGroup("com.happiest.minds");
        request.setArtifact("employee-project");
        request.setName("Employee Application");
        request.setDescription("An application to maintain Employee record");
        request.setPackagingName("org.happiest.minds");
        request.setPackagingType("jar");
        request.setDependencies(List.of("Web", "GraphQL"));

        BindingResult bindingResult = mock(BindingResult.class);
        when(!bindingResult.hasErrors()).thenReturn(true);
    }
    @Test
    void testDownloadTemplateWithValidationErrors() {
        SpringInitializrRequest request = new SpringInitializrRequest();
        request.setGroup("");
        request.setArtifact("");
        request.setName("");
        request.setDescription("");
        request.setPackagingName("");
        request.setPackagingType("");
        request.setDependencies(List.of());

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("SpringInitializrRequest", "group", "group should not be blank")));

        ResponseEntity<SpringInitializrResponse> response = springInitializrController.downloadTemplate(new MockHttpServletResponse(), request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetDependencies_Success() {
        when(springInitializrService.getDependencies()).thenReturn(List.of("dependency1", "dependency2"));
        ResponseEntity<?> responseEntity = springInitializrController.getDependencies();
        verify(springInitializrService).getDependencies();
        Map<String, List<String>> expectedResponse = Map.of("dependencies", List.of("dependency1", "dependency2"));
        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}