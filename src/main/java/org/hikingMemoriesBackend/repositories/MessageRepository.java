package org.hikingMemoriesBackend.repositories;

import org.hikingMemoriesBackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
