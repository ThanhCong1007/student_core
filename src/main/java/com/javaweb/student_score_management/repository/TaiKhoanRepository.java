package com.javaweb.student_score_management.repository;

import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoanEntity, Integer> {
    Optional<TaiKhoanEntity> findByUsername(String username);
    boolean existsByUsername(String username);

}