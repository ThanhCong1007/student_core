package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.DTO.MonHocDTO;
import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import com.javaweb.student_score_management.repository.GiangVienRepository;
import com.javaweb.student_score_management.repository.MonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonHocService {
    @Autowired
    private MonHocRepository monHocRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private DiemService diemService;


    //Admin
    // Lấy danh sách Môn Hoc
    public List<MonHocDTO> getAllMonHoc() {
        return monHocRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean existsByTenMH(String tenMH) {
        return monHocRepository.existsByTenMH(tenMH);
    }

    public MonHocDTO getMonHocById(Integer id) {
        return monHocRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public boolean createMonHoc(MonHocDTO monHocDTO) {
        try {
            MonHocEntity monHoc = new MonHocEntity();
            monHoc.setTenMH(monHocDTO.getTenMH());
            monHoc.setSoTinChi(monHocDTO.getSoTinChi());
            if (monHocDTO.getMaGV() != null) {
                GiangVienEntity giangVien = giangVienRepository.findById(monHocDTO.getMaGV())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + monHocDTO.getMaGV()));
                monHoc.setMaGV(giangVien);
            }
            monHocRepository.save(monHoc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateMonHoc(Integer id, MonHocDTO monHocDTO) {

        if (diemService.existsByMaMH(id)) {
            throw new IllegalStateException("Môn học đã có điểm, không thể sửa!");
        }

        return monHocRepository.findById(id).map(monHoc -> {
            monHoc.setTenMH(monHocDTO.getTenMH());
            monHoc.setSoTinChi(monHocDTO.getSoTinChi());
            if (monHocDTO.getMaGV() != null) {
                GiangVienEntity giangVien = giangVienRepository.findById(monHocDTO.getMaGV())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + monHocDTO.getMaGV()));
                monHoc.setMaGV(giangVien);
            }
            monHocRepository.save(monHoc);
            return true;
        }).orElse(false);
    }

    public boolean deleteMonHoc(Integer id) {

        if (diemService.existsByMaMH(id)) {
            throw new IllegalStateException("Môn học đã có điểm, không thể xóa!");
        }

        if (monHocRepository.existsById(id)) {
            monHocRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MonHocDTO convertToDTO(MonHocEntity monHoc) {
        return new MonHocDTO(
                monHoc.getMaMH(),
                monHoc.getTenMH(),
                monHoc.getSoTinChi(),
                monHoc.getMaGV() != null ? monHoc.getMaGV().getMaGV() : null,
                monHoc.getMaGV().getTenGV()
        );
    }

    //GV lấy DSMH mình dạy
    public List<MonHocDTO> getMonHocByMaGV(int maGV) {

        GiangVienEntity giangVienEntity = giangVienRepository.findById(maGV).get();

        List<MonHocEntity> listMH = monHocRepository.findByMaGV(giangVienEntity);

        List<MonHocDTO> monHocDTOList = new ArrayList<>();

        for (MonHocEntity monHocEntity : listMH) {
            MonHocDTO monHocDTO = convertToDTO(monHocEntity);
            monHocDTOList.add(monHocDTO);
        }

        return monHocDTOList;

    }

    public List<MonHocDTO> findMonHocByTenMHGanDung(String tenMH) {
        List<MonHocEntity> monHocList = monHocRepository.findByTenMHContainingIgnoreCase(tenMH);
        return monHocList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<MonHocEntity> timKiemMonHocTheoTen(String tenMH) {
        return monHocRepository.findByTenMHContainingIgnoreCase(tenMH);
    }
}
