package org.happiest.minds.springinitializr.service;

import jakarta.servlet.http.HttpServletResponse;
import org.happiest.minds.springinitializr.config.SpringDependencyConfig;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.happiest.minds.springinitializr.utility.FileUtility;
import org.happiest.minds.springinitializr.utility.XMLUtility;
import org.happiest.minds.springinitializr.utility.ZipUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SpringInitializrServiceTest {

    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private XMLUtility xmlUtility;

    @Mock
    private ZipUtility zipUtility;

    @Mock
    private SpringInitializrRequest request;

    @InjectMocks
    private SpringInitializrService springInitializrService;

    @Mock
    private FileUtility fileUtility;

    @Mock
    private SpringDependencyConfig springDependencyConfig;

    @AfterEach
    public void validate() {
        validateMockitoUsage();
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        springInitializrService = new SpringInitializrService(fileUtility, xmlUtility, zipUtility, springDependencyConfig);
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
    void testUpdateDirectoryValidInputs() throws Exception {
        SpringInitializrRequest mockRequest = new SpringInitializrRequest();

        Method updateDirectoryMethod = SpringInitializrService.class.getDeclaredMethod(
                "updateDirectory", String.class, String.class, String.class,
                String.class, String.class, String.class, SpringInitializrRequest.class);
        updateDirectoryMethod.setAccessible(true);

        updateDirectoryMethod.invoke(springInitializrService, "mainDirPath", "javaDirPath", "mainClassFilePath",
                "mainClassFilename", "application", "mainClassName", mockRequest);
    }

    @Test
    void testUpdateDirectory() {
        when(request.getArtifact()).thenReturn("myArtifact");
        when(request.getPackagingName()).thenReturn("com.example");
        when(request.getDependencies()).thenReturn(Collections.singletonList("dependency"));

        String mainDirPath = "main/dir/path/";
        String javaDirPath = "java/dir/path/";
        String mainClassFilePath = "main/class/file/path/MainClass.java";
        String mainClassFilename = "MainClass.java";
        String application = "Application";
        String mainClassName = "MainClass";

        springInitializrService.updateDirectory(mainDirPath, javaDirPath, mainClassFilePath, mainClassFilename,
                application, mainClassName, request);

        verify(fileUtility).deleteDirectory(mainDirPath);
        verify(fileUtility).createDirectories(anyString());
        verify(fileUtility).copyDirectory(anyString(), anyString());
        verify(fileUtility).moveOrRenameFile(anyString(), anyString());
    }

    @Test
    void testUpdateDirectory_Exception() {
        when(request.getArtifact()).thenReturn("myArtifact");
        when(request.getPackagingName()).thenReturn("com.example");
        when(request.getDependencies()).thenReturn(Collections.singletonList("dependency"));

        String mainDirPath = "main/dir/path/";
        String javaDirPath = "java/dir/path/";
        String mainClassFilePath = "main/class/file/path/MainClass.java";
        String mainClassFilename = "MainClass.java";
        String application = "Application";
        String mainClassName = "MainClass";

        doThrow(new RuntimeException("Mocked exception")).when(fileUtility).copyDirectory(anyString(), anyString());
        springInitializrService.updateDirectory(mainDirPath, javaDirPath, mainClassFilePath, mainClassFilename,
                application, mainClassName, request);

    }

    @Test
    void testGetDependencies() {
        List<String> expectedDependencies = springDependencyConfig.getDependencies().keySet().stream().toList();
        List<String> actualDependencies = springInitializrService.getDependencies();
        assertEquals(expectedDependencies, actualDependencies);
    }

    @Test
    void testDependencyPresentPositive() {
        List<String> dependencies = springDependencyConfig.getDependencies().keySet().stream().toList();
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertTrue(result);
    }

    @Test
    void testDependencyPresentNegative() {
        List<String> dependencies = List.of("web","graphql", "ABC");
        boolean result = springInitializrService.dependencyPresent(dependencies);
        assertFalse(result);
    }

}