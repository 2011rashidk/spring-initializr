package org.happiest.minds.springinitializr.service;

import org.happiest.minds.springinitializr.constant.Constants;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.utility.FileUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SpringInitializrService {

    public ResponseEntity<?> downloadTemplate(SpringInitializrRequest springInitializrRequest) {
        try {

            FileUtility.copyDirectory(Constants.REFERENCE_DIR_PATH, Constants.DOWNLOAD_DIR_PATH);

            FileUtility.deleteDirectory(Constants.DOWNLOAD_DIR_MAIN_PATH);

            String groupIdPath = springInitializrRequest.getPackagingName().replaceAll("[.]", "//");

            String artifactId = springInitializrRequest.getArtifact().replaceAll("[^a-zA-Z]", "");

            String downloadDirMainClassPath = Constants.DOWNLOAD_DIR_JAVA_PATH + groupIdPath;

            FileUtility.createDirectories(downloadDirMainClassPath);

            FileUtility.copyDirectory(Constants.REFERENCE_DIR_MAIN_FILE_PATH, downloadDirMainClassPath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
