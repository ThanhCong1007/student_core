package com.javaweb.student_score_management.repository;

import com.javaweb.student_score_management.entity.SinhVienEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVienEntity, Integer> {
    List<SinhVienEntity> findAll();
    Optional<SinhVienEntity> findById(Integer maSV);
    boolean existsByEmail(String email);
//    boolean existsByGiangVienId(Integer giangVienId);

}
