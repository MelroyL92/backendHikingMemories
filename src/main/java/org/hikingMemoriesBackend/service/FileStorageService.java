package org.hikingMemoriesBackend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


// made to be able to change it to storage in the cloud easier in the future
public interface FileStorageService {
    String uploadFile(MultipartFile file);
    Resource loadFile(String fileName);
}
