package org.hikingMemoriesBackend.controllers;


import org.hikingMemoriesBackend.repositories.CountryRepository;
import org.hikingMemoriesBackend.repositories.PhotoRepository;
import org.hikingMemoriesBackend.service.CountryService;
import org.hikingMemoriesBackend.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class PhotoController {

    private final String UPLOAD_DIR = "uploads/";
    private final PhotoService photoService;
    private final CountryRepository countryRepository;
    private final PhotoRepository photoRepository;
    private final CountryService countryService;


    public PhotoController(PhotoService photoService, CountryRepository countryRepository, PhotoRepository photoRepository, CountryService countryService) {
        this.photoService = photoService;
        this.photoRepository = photoRepository;
        this.countryRepository = countryRepository;
        this.countryService = countryService;
    }


//    @GetMapping("/uploads/{fileName:.+}")
//    public ResponseEntity<Resource> getPhoto(@PathVariable String fileName) {
//        try {
//            Path filePath = Paths.get(UPLOAD_DIR + fileName);
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG)
//                        .body(resource);
//
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (MalformedURLException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/uploads/{country}")
    public ResponseEntity<List<String>> getImagesForCountry(@PathVariable String country) {

        File countryFolder = new File("uploads/" + country);

        if (!countryFolder.exists() || !countryFolder.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        File[] imageFiles = countryFolder.listFiles((dir, name) ->
                name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"));

        if (imageFiles == null || imageFiles.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<String> imageUrls = Arrays.stream(imageFiles)
                .map(imageFile -> "/uploads/" + country + "/" + imageFile.getName())
                .collect(Collectors.toList());

        return ResponseEntity.ok(imageUrls);
    }
    @PostMapping("/uploads")
    public ResponseEntity<Map<String, String>> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("country") String country,
            @RequestParam("title") String title) {
        try {
            String fileUrl = photoService.uploadPhoto(file, country, title);

            Map<String, String> response = Map.of(
                    "fileUrl", fileUrl,
                    "message", "Upload successful"
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error uploading file"));
        }
    }
}