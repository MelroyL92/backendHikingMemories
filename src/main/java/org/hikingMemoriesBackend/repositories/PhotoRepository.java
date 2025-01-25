package org.hikingMemoriesBackend.repositories;

import org.hikingMemoriesBackend.entities.Country;
import org.hikingMemoriesBackend.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByFilename(String title);
    List<Photo> findByCountry(Country country);
}
