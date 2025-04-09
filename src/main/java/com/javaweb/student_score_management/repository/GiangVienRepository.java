package com.javaweb.student_score_management.repository;

import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiangVienRepository extends JpaRepository<GiangVienEntity, Integer> {
    boolean existsByEmail(String email);

}
