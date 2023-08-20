package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static void replaceTextInFile(String filePath, String text, String replacement) {
        try {
            Path path = Paths.get(filePath);
            Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8);
            List<String> list = stream.map(line -> line.replace(text, replacement)).collect(Collectors.toList());
            Files.write(path, list, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void moveOrRenameFile(String source, String destination) {
        try {
            FileUtils.moveFile(new File(source), new File(destination));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void renameDir(String existingDirName, String newDirName) {
        try {
            File file = new File(existingDirName);
            file.renameTo(new File(newDirName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void deleteDirContents(String dirName) {
        try {
            FileUtils.cleanDirectory(new File(dirName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
