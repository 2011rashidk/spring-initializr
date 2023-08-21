package org.happiest.minds.springinitializr.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.constant.Constants;
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

@Service
@Slf4j
public class SpringInitializrService {

    @Autowired
    XMLUtility xmlUtility;

    @Autowired
    ZipUtility zipUtility;

    public ResponseEntity<SpringInitializrResponse> downloadTemplate(HttpServletResponse httpServletResponse, SpringInitializrRequest springInitializrRequest) {
        try {

            FileUtility.copyDirectory(Constants.REFERENCE_DIR_PATH, Constants.DOWNLOAD_DIR_PATH);

            updateDirectory(Constants.DOWNLOAD_DIR_MAIN_PATH, Constants.DOWNLOAD_DIR_MAIN_JAVA_PATH,
                    Constants.REFERENCE_DIR_MAIN_FILE_PATH, Constants.MAIN_CLASS_FILENAME,
                    Constants.APPLICATION, Constants.SPRING_TEMPLATE_APPLICATION, springInitializrRequest);

            updateDirectory(Constants.DOWNLOAD_DIR_TEST_PATH, Constants.DOWNLOAD_DIR_TEST_JAVA_PATH,
                    Constants.REFERENCE_DIR_TEST_FILE_PATH, Constants.TEST_CLASS_FILENAME,
                    Constants.APPLICATION_TESTS, Constants.SPRING_TEMPLATE_APPLICATION_TESTS, springInitializrRequest);

            xmlUtility.updateXMLElementValue(Constants.POM_XML_FILEPATH, springInitializrRequest);

            String newFolderName = Constants.DOWNLOAD_DIR_PATH + springInitializrRequest.getArtifact();
            FileUtility.renameDir(Constants.PROJECT_DIR_NAME, newFolderName);

            zipUtility.zipFolder(newFolderName);

            FileUtility.downloadFile(httpServletResponse, newFolderName);

            FileUtility.deleteDirContents(Constants.DOWNLOAD_DIR_PATH);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(new SpringInitializrResponse(HttpStatus.OK, Constants.SPRING_TEMPLATE_READY_TO_DOWNLOAD), HttpStatus.OK);
    }

    private static void updateDirectory(String mainDirPath, String javaDirPath,
                                        String mainClassFilePath, String mainClassFilename,
                                        String application, String mainClassName,
                                        SpringInitializrRequest springInitializrRequest) {
        try {
            String artifactId = springInitializrRequest.getArtifact().replaceAll("[^a-zA-Z]", "");
            String packagingNamePath = springInitializrRequest.getPackagingName().replaceAll("[.]", "//");
            FileUtility.deleteDirectory(mainDirPath);
            String downloadDirTestClassPath = javaDirPath + packagingNamePath;
            FileUtility.createDirectories(downloadDirTestClassPath);
            FileUtility.copyDirectory(mainClassFilePath, downloadDirTestClassPath);
            String testClassFilenamePath = downloadDirTestClassPath + mainClassFilename;
            String newTestClassName = StringUtils.capitalize(artifactId) + application;
            FileUtility.replaceTextInFile(testClassFilenamePath, mainClassName, newTestClassName);
            FileUtility.replaceTextInFile(testClassFilenamePath, Constants.EXISTING_PACKAGE_NAME, springInitializrRequest.getPackagingName());
            String newTestClassFilename = downloadDirTestClassPath + "/" + newTestClassName + Constants.JAVA;
            FileUtility.moveOrRenameFile(testClassFilenamePath, newTestClassFilename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<String> getDependencies() {
        return List.of(Constants.WEB, Constants.GRAPH_QL, Constants.THYMELEAF,
                Constants.SECURITY, Constants.JPA, Constants.JDBC,
                Constants.MY_SQL, Constants.H_2, Constants.VALIDATION, Constants.LOMBOK);
    }

    public boolean dependencyPresent(List<String> dependencies) {
        return new HashSet<>(getDependencies()).containsAll(dependencies);
    }

}
