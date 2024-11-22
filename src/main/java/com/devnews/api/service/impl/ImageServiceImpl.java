package com.devnews.api.service.impl;

import com.devnews.api.service.ImageService;
import com.devnews.api.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Override
    public String uploadImage(Long postId, MultipartFile file) {
        try {
            Path directory = Paths.get(uploadDir);

            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String extension = getFileExtension(file.getOriginalFilename());
            String uniqueFilename = postId + extension;

            Path filePath = directory.resolve(uniqueFilename);
            Files.write(filePath, file.getBytes());

            return uniqueFilename;
        } catch (Exception e) {
            log.error("Erro ao salvar imagem", e);
            throw new RuntimeException("Erro ao salvar imagem");
        }
    }

    @Override
    public Resource getImage(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Arquivo não encontrado: " + filename);
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return resource;
        } catch (Exception e) {
            log.error("Erro ao baixar imagem", e);
            throw new RuntimeException("Erro ao baixar imagem");
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
