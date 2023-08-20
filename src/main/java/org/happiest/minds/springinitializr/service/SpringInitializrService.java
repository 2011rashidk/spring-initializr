package org.happiest.minds.springinitializr.service;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.constant.Constants;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.utility.FileUtility;
import org.happiest.minds.springinitializr.utility.XMLUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class SpringInitializrService {

    @Autowired
    XMLUtility xmlUtility;

    public ResponseEntity<?> downloadTemplate(SpringInitializrRequest springInitializrRequest) {
        try {

            FileUtility.copyDirectory(Constants.REFERENCE_DIR_PATH, Constants.DOWNLOAD_DIR_PATH);

            FileUtility.deleteDirectory(Constants.DOWNLOAD_DIR_MAIN_PATH);

            String groupIdPath = springInitializrRequest.getPackagingName().replaceAll("[.]", "//");

            String artifactId = springInitializrRequest.getArtifact().replaceAll("[^a-zA-Z]", "");

            String downloadDirMainClassPath = Constants.DOWNLOAD_DIR_JAVA_PATH + groupIdPath;

            FileUtility.createDirectories(downloadDirMainClassPath);

            FileUtility.copyDirectory(Constants.REFERENCE_DIR_MAIN_FILE_PATH, downloadDirMainClassPath);

            String mainClassFilename = Constants.DOWNLOAD_DIR_MAIN_FILE_PATH + Constants.MAIN_CLASS_FILENAME;

            String newMainClassName = StringUtils.capitalize(artifactId) + Constants.APPLICATION;

            FileUtility.replaceTextInFile(mainClassFilename, Constants.SPRING_TEMPLATE_APPLICATION, newMainClassName);

            FileUtility.replaceTextInFile(mainClassFilename, Constants.EXISTING_PACKAGE_NAME, springInitializrRequest.getPackagingName());

            String newMainClassFilename = newMainClassName + Constants.JAVA;

            FileUtility.moveOrRenameFile(mainClassFilename, newMainClassFilename);

            xmlUtility.updateXMLElementValue(Constants.POM_XML_FILEPATH, springInitializrRequest);

            String newFolderName = Constants.DOWNLOAD_DIR_PATH + springInitializrRequest.getArtifact();

            FileUtility.renameDir(Constants.PROJECT_DIR_NAME, newFolderName);

//            FileUtility.deleteDirContents(Constants.DOWNLOAD_DIR_PATH);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
