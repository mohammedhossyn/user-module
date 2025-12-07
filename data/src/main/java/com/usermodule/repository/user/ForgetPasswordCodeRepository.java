package com.usermodule.repository.user;

import com.usermodule.model.user.ForgetPasswordCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Date;
import java.util.Optional;

public interface ForgetPasswordCodeRepository extends JpaRepository<ForgetPasswordCodeEntity, Long>, ListPagingAndSortingRepository<ForgetPasswordCodeEntity, Long> {
    boolean existsByUsernameAndCode(String username, String code);
    Optional<ForgetPasswordCodeEntity> findByUsernameAndCode(String username, String code);

    @Query(value = " SELECT fpce FROM ForgetPasswordCodeEntity fpce " +
            " WHERE fpce.username = :username AND fpce.code = :code " +
            " AND fpce.createdDate > :nowMinusFiveMinute AND fpce.isUsed = 0")
    Optional<ForgetPasswordCodeEntity> findByUsernameAndCode(String username, String code, Date nowMinusFiveMinute);

}
