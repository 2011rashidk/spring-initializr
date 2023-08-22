package org.happiest.minds.springinitializr.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.happiest.minds.springinitializr.constant.StringConstants.*;


@ExtendWith(SpringExtension.class)
class SpringInitializrServiceTest {

    @InjectMocks
    private SpringInitializrService springInitializrService;

    @Test
    public void testGetDependencies() {
        List<String> expectedDependencies = List.of(WEB, GRAPH_QL, THYMELEAF,
                SECURITY, JPA, JDBC, MY_SQL, H_2, VALIDATION, LOMBOK);
        List<String> actualDependencies = springInitializrService.getDependencies();
        assertEquals(expectedDependencies, actualDependencies);
    }

    @Test
    public void testDependencyPresentPositive() {
        List<String> dependencies = List.of(WEB, GRAPH_QL, THYMELEAF,
                SECURITY, JPA, JDBC, MY_SQL, H_2, VALIDATION, LOMBOK);
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertTrue(result);
    }

    @Test
    public void testDependencyPresentNegative() {
        List<String> dependencies = List.of(
                WEB, SECURITY, "ABC");
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertFalse(result);
    }

}