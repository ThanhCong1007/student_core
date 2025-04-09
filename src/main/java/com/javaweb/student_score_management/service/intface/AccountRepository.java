package com.javaweb.student_score_management.service.intface;

import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<TaiKhoanEntity, Integer> {
    Optional<TaiKhoanEntity> findByUsername(String Username);
}
