package com.usermodule.repository.message;

import com.usermodule.model.message.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>, ListPagingAndSortingRepository<MessageEntity, Long> {
}
