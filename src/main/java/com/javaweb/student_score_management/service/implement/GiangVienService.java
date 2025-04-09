package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.repository.GiangVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiangVienService {

    @Autowired
    private GiangVienRepository giangVienRepository;

    public GiangVienEntity findByMaGV(Integer maGV) {
        return giangVienRepository.findById(maGV).orElse(null);
    }

    public void save(GiangVienEntity giangVien) {
        giangVienRepository.save(giangVien);
    }
}