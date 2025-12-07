package com.usermodule.repository.auth;

import com.usermodule.model.auth.LoginHistoryEntity;
import com.usermodule.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long>, ListPagingAndSortingRepository<LoginHistoryEntity, Long> {

    Page<LoginHistoryEntity> findByUser(Pageable pageable, UserEntity user);

}
