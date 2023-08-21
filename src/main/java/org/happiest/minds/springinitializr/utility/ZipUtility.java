package org.happiest.minds.springinitializr.utility;

import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.constant.Constants;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Slf4j
public class ZipUtility {

    public void zipFolder(String projectDirectory) {
        try {
            Path sourceFolderPath = Path.of(projectDirectory);
            String zipName = projectDirectory + Constants.ZIP;
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(Path.of(zipName).toFile()));
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zipOutputStream.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                    Files.copy(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
            zipOutputStream.close();
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }

    }
}
