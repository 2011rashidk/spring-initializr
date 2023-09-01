package org.happiest.minds.springinitializr.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.happiest.minds.springinitializr.utility.FileUtility;
import org.happiest.minds.springinitializr.utility.XMLUtility;
import org.happiest.minds.springinitializr.utility.ZipUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

import static org.happiest.minds.springinitializr.enums.Constants.*;
import static org.happiest.minds.springinitializr.constant.StringConstants.*;

@Service
@Slf4j
public class SpringInitializrService {

    @Autowired
    FileUtility fileUtility;

    @Autowired
    XMLUtility xmlUtility;

    @Autowired
    ZipUtility zipUtility;

    public SpringInitializrService(FileUtility fileUtility, XMLUtility xmlUtility, ZipUtility zipUtility) {
        this.fileUtility = fileUtility;
        this.zipUtility = zipUtility;
        this.xmlUtility = xmlUtility;
    }

    public ResponseEntity<SpringInitializrResponse> downloadTemplate(HttpServletResponse httpServletResponse, SpringInitializrRequest springInitializrRequest) {
        try {

            if (!dependencyPresent(springInitializrRequest.getDependencies())) {
                log.error(DEPENDENCY_NOT_PRESENT.getValue());
                return new ResponseEntity<>(new SpringInitializrResponse(HttpStatus.BAD_REQUEST, DEPENDENCY_NOT_PRESENT.getValue()), HttpStatus.BAD_REQUEST);
            }

            fileUtility.copyDirectory(REFERENCE_DIR_PATH.getValue(), DOWNLOAD_DIR_PATH.getValue());

            updateDirectory(DOWNLOAD_DIR_MAIN_PATH.getValue(), DOWNLOAD_DIR_MAIN_JAVA_PATH.getValue(),
                    REFERENCE_DIR_MAIN_FILE_PATH.getValue(), MAIN_CLASS_FILENAME.getValue(),
                    APPLICATION.getValue(), SPRING_TEMPLATE_APPLICATION.getValue(), springInitializrRequest);

            updateDirectory(DOWNLOAD_DIR_TEST_PATH.getValue(), DOWNLOAD_DIR_TEST_JAVA_PATH.getValue(),
                    REFERENCE_DIR_TEST_FILE_PATH.getValue(), TEST_CLASS_FILENAME.getValue(),
                    APPLICATION_TESTS.getValue(), SPRING_TEMPLATE_APPLICATION_TESTS.getValue(), springInitializrRequest);

            xmlUtility.updateXMLElementValue(POM_XML_FILEPATH.getValue(), springInitializrRequest);

            String newFolderName = DOWNLOAD_DIR_PATH.getValue() + springInitializrRequest.getArtifact();

            zipUtility.zipFolder(newFolderName);

            fileUtility.downloadFile(httpServletResponse, newFolderName);

            fileUtility.deleteDirContents(DOWNLOAD_DIR_PATH.getValue());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(new SpringInitializrResponse(HttpStatus.OK, SPRING_TEMPLATE_READY_TO_DOWNLOAD.getValue()), HttpStatus.OK);
    }

    public void updateDirectory(String mainDirPath, String javaDirPath,
                                String mainClassFilePath, String mainClassFilename,
                                String application, String mainClassName,
                                SpringInitializrRequest springInitializrRequest) {
        try {
            String artifactId = springInitializrRequest.getArtifact().replaceAll("[^a-zA-Z]", "");
            String packagingNamePath = springInitializrRequest.getPackagingName().replaceAll("[.]", "//");
            fileUtility.deleteDirectory(mainDirPath);
            String downloadDirTestClassPath = javaDirPath + packagingNamePath;
            fileUtility.createDirectories(downloadDirTestClassPath);
            fileUtility.copyDirectory(mainClassFilePath, downloadDirTestClassPath);
            String testClassFilenamePath = downloadDirTestClassPath + mainClassFilename;
            String newTestClassName = StringUtils.capitalize(artifactId) + application;
            fileUtility.replaceTextInFile(testClassFilenamePath, mainClassName, newTestClassName);
            fileUtility.replaceTextInFile(testClassFilenamePath, EXISTING_PACKAGE_NAME.getValue(), springInitializrRequest.getPackagingName());
            String newTestClassFilename = downloadDirTestClassPath + SLASH.getValue() + newTestClassName + JAVA.getValue();
            fileUtility.moveOrRenameFile(testClassFilenamePath, newTestClassFilename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<String> getDependencies() {
        List<String> dependencies = List.of(WEB, GRAPH_QL, THYMELEAF,
                SECURITY, JPA, JDBC,
                MY_SQL, H_2, VALIDATION, LOMBOK);
        log.info("Dependencies: {}", dependencies);
        return dependencies;
    }

    public boolean dependencyPresent(List<String> dependencies) {
        return new HashSet<>(getDependencies()).containsAll(dependencies);
    }

}
