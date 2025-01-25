package org.hikingMemoriesBackend.service;


import jakarta.persistence.EntityNotFoundException;
import org.hikingMemoriesBackend.entities.Country;
import org.hikingMemoriesBackend.entities.Photo;
import org.hikingMemoriesBackend.repositories.CountryRepository;
import org.hikingMemoriesBackend.repositories.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService{
    private final String UPLOAD_DIR = "uploads/";

    private final PhotoRepository photoRepository;
    private final CountryRepository countryRepository;


    public PhotoService(PhotoRepository photoRepository, CountryRepository countryRepository){
    this.photoRepository = photoRepository;
    this.countryRepository = countryRepository;

    }

    public String uploadPhoto(MultipartFile file, String country, String title) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG and PNG are allowed.");
        }

        Optional<Photo> existingPhoto = photoRepository.findByFilename(title);
        if (existingPhoto.isPresent()) {
            throw new IllegalArgumentException("Please select a valid name. The name '" + title + "' already exists.");
        }


        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9]", "_");
        String filename = sanitizedTitle + ".jpg";
        Path countryDir = Paths.get(UPLOAD_DIR, country);
        Files.createDirectories(countryDir);
        Path filePath = countryDir.resolve(filename);

        try (BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(filePath))) {
            stream.write(file.getBytes());
        }

        Photo photo = new Photo();
        photo.setFilename(title);
        photo.setFileUrl("/uploads/" + country + "/" + filename);
        photo.setCountry(country);
        photoRepository.save(photo);

        return photo.getFileUrl();
    }

    public List<Photo> getPhotoByCountry(String country){
        Optional<Country>countryFound = countryRepository.findByName(country);
        if (countryFound.isPresent()){
            return photoRepository.findByCountry(countryFound.get());
        } else {
            throw new EntityNotFoundException("Country not found");
        }

    }
}


//    @Override
//    public String uploadFile(MultipartFile file) {
//        try {
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = Paths.get(UPLOAD_DIR + fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, file.getBytes());
//            return "/uploads/" + fileName;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file", e);
//        }
//    }
//

//}

