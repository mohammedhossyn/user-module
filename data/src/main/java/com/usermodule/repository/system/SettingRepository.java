package com.usermodule.repository.system;

import com.usermodule.model.system.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<SettingEntity, Long>, ListPagingAndSortingRepository<SettingEntity, Long> {
}
