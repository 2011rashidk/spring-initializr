package org.happiest.minds.springinitializr.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileUtilityTest {

    @MockBean
    private FileUtility fileUtility;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fileUtility = new FileUtility();
    }

    @Test
    public void testCopyDirectory() throws IOException {
        String sourceDir = "path/to/source/directory";
        String destDir = "path/to/destination/directory";
        doNothing().when(logger).info(anyString(), any(), any());
        doNothing().when(logger).error(anyString());

        fileUtility.copyDirectory(sourceDir, destDir);

        verify(logger, never()).error(anyString());
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        String dirName = "path/to/directory";

        doNothing().when(logger).error(anyString());

        fileUtility.deleteDirectory(dirName);

        verify(logger, never()).error(anyString());
    }


    @Test
    public void testCreateDirectories() throws Exception {
        String directories = "path/to/directories";
        doNothing().when(logger).error(anyString());
        fileUtility.createDirectories(directories);
        verify(logger, never()).error(anyString());

    }

}