package org.happiest.minds.springinitializr.service;

import jakarta.servlet.http.HttpServletResponse;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.happiest.minds.springinitializr.utility.FileUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.happiest.minds.springinitializr.constant.StringConstants.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class SpringInitializrServiceTest {

    @Mock
    private HttpServletResponse mockResponse;

    @InjectMocks
    private SpringInitializrService springInitializrService;

    @InjectMocks
    private FileUtility fileUtility;

    @AfterEach
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    void testDownloadTemplateDependencyNotPresent() {
        SpringInitializrRequest springInitializrRequest = new SpringInitializrRequest();
        springInitializrRequest.setDependencies(List.of("Web", "ABC"));

        boolean result = springInitializrService.dependencyPresent(springInitializrRequest.getDependencies());
        assertFalse(result);
        ResponseEntity<SpringInitializrResponse> responseEntity = springInitializrService.downloadTemplate(mockResponse, springInitializrRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Any or none of the dependency present", responseEntity.getBody().getMessage());
    }

    @Test
    void testGetDependencies() {
        List<String> expectedDependencies = List.of(WEB, GRAPH_QL, THYMELEAF,
                SECURITY, JPA, JDBC, MY_SQL, H_2, VALIDATION, LOMBOK);
        List<String> actualDependencies = springInitializrService.getDependencies();
        assertEquals(expectedDependencies, actualDependencies);
    }

    @Test
    void testDependencyPresentPositive() {
        List<String> dependencies = List.of(WEB, GRAPH_QL, THYMELEAF,
                SECURITY, JPA, JDBC, MY_SQL, H_2, VALIDATION, LOMBOK);
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertTrue(result);
    }

    @Test
    void testDependencyPresentNegative() {
        List<String> dependencies = List.of(
                WEB, SECURITY, "ABC");
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertFalse(result);
    }

    @Test
    void testUpdateDirectoryValidInputs() throws Exception {
        SpringInitializrRequest mockRequest = new SpringInitializrRequest();

        Method updateDirectoryMethod = SpringInitializrService.class.getDeclaredMethod(
                "updateDirectory", String.class, String.class, String.class,
                String.class, String.class, String.class, SpringInitializrRequest.class);
        updateDirectoryMethod.setAccessible(true);

        updateDirectoryMethod.invoke(springInitializrService, "mainDirPath", "javaDirPath", "mainClassFilePath",
                "mainClassFilename", "application", "mainClassName", mockRequest);
    }

}