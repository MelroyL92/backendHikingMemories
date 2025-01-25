package org.hikingMemoriesBackend.repositories;

import org.hikingMemoriesBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
