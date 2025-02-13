package com.electronic.store.file;

import jakarta.annotation.Nonnull;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {

    Resource loadLatestFile(String userId, String fileSubPath);
    void deleteUserFolder(String userId) throws IOException;
    String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath
    );
}
