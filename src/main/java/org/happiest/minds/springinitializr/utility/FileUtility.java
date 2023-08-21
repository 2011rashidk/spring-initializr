package org.happiest.minds.springinitializr.utility;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.happiest.minds.springinitializr.constant.Constants;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
            log.info("Filepath: {}, text: {}, replacement text: {}", filePath, text, replacement);
            Path path = Paths.get(filePath);
            Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8);
            List<String> list = stream.map(line -> line.replace(text, replacement)).toList();
            Files.write(path, list, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void moveOrRenameFile(String source, String destination) {
        try {
            log.info("Source: {}, destination: {}", source, destination);
            FileUtils.moveFile(new File(source), new File(destination));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void renameDir(String existingDirName, String newDirName) {
        try {
            log.info("Existing directory name: {}, new directory name: {}", existingDirName, newDirName);
            File file = new File(existingDirName);
            boolean isRenamed = file.renameTo(new File(newDirName));
            log.info("Is file renamed: {}", isRenamed);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void deleteDirContents(String dirName) {
        try {
            log.info("Directory name: {}", dirName);
            FileUtils.cleanDirectory(new File(dirName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void downloadFile(HttpServletResponse httpServletResponse, String fileName) {
        try {
            log.info("Filename: {}", fileName);
            String zipName = fileName + Constants.ZIP;
            Path path = Paths.get(zipName);
            String contentType;
            contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            httpServletResponse.setContentType(contentType);
            httpServletResponse.setContentLengthLong(Files.size(path));
            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                    .filename(path.getFileName().toString())
                    .build()
                    .toString());
            Files.copy(path, httpServletResponse.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
