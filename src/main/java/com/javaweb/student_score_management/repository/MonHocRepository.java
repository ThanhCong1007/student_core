package com.javaweb.student_score_management.repository;

import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonHocRepository extends JpaRepository<MonHocEntity, Integer> {
    boolean existsByTenMH(String tenMH);
    List<MonHocEntity> findByMaGV(GiangVienEntity maGV);

    List<MonHocEntity> findByTenMHContainingIgnoreCase(String tenMH);


}
