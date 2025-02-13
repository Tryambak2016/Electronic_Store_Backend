package com.electronic.store.file;
import com.electronic.store.exceptions.BadApiRequest;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Value("${application.file.uploads.media-output-path}")
    private String fileUploadPath;

    @Override
    public String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath
    ) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the target folder: " + targetFolder);
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to: " + targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    // serve file

    @Override
    public Resource loadLatestFile(String userId, String fileSubPath) {
        try {
            Path userDirectory = Paths.get(fileUploadPath, fileSubPath).normalize();
            File directory = userDirectory.toFile();

            if (!directory.exists() || !directory.isDirectory()) {
                return null;
            }

            File[] files = directory.listFiles((FilenameFilter) (dir, name) -> new File(dir, name).isFile());

            if (files == null || files.length == 0) {
                return null;
            }

            File latestFile = null;
            for (File file : files) {
                if (latestFile == null || file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                }
            }

            if (latestFile != null) {
                Path filePath = latestFile.toPath();
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists() && resource.isReadable()) {
                    return resource;
                }
            }
        } catch (MalformedURLException e) {
            return null;
        }
        return null;
    }

    @Override
    public void deleteUserFolder(String userId) throws IOException {
        String fileSubPath = "users" + File.separator + userId;
        Path userFolderPath = Paths.get(fileUploadPath, fileSubPath).normalize();

        if (Files.exists(userFolderPath)) {
            try (Stream<Path> paths = Files.walk(userFolderPath)) {
                paths.sorted(Comparator.reverseOrder()) // Process files first, then directories
                        .map(Path::toFile)
                        .forEach(file -> {
                            try {
                                if (!file.delete()) {
                                    System.err.println("Failed to delete file: " + file.getAbsolutePath());
                                }
                            } catch (Exception e) {
                                throw new BadApiRequest("Error deleting file");
                            }
                        });
            }
        } else {
            log.warn("Folder for user " + userId + " does not exist.");
        }
    }


}

