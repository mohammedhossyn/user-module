package com.usermodule.repository.attachment;

import com.usermodule.model.attachment.AttachmentCategory;
import com.usermodule.model.attachment.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long>, ListPagingAndSortingRepository<AttachmentEntity, Long> {
    AttachmentEntity findByUser_UsernameAndCategory(String userUsername, AttachmentCategory category);

   List<AttachmentEntity> findAllByUser_Username(String userUsername);
}
