package org.hikingMemoriesBackend.repositories;

import org.hikingMemoriesBackend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    Optional<Country> findByName(String name);

}
