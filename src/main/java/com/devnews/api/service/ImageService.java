package com.devnews.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(Long postId, MultipartFile file);
    Resource getImage(String filename);
}
