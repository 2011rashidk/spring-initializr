package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtility {

    public static void copyDirectory(String sourceDir, String destDir) {
        try {
            log.info("Source directory: {}, Destination directory: {}", sourceDir, destDir);
            FileUtils.copyDirectory(new File(sourceDir), new File(destDir));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void deleteDirectory(String dirName) {
        try {
            log.info("Directory name: {}", dirName);
            FileUtils.deleteDirectory(new File(dirName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void createDirectories(String directories) {
        try {
            log.info("Directories: {}", directories);
            Files.createDirectories(Path.of(directories));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
