package com.restliz.task.repository;

import com.restliz.task.entity.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends org.springframework.data.jpa.repository.JpaRepository<Message, Long> {
}
