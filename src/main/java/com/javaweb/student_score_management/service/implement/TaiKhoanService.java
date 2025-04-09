package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.DTO.TaiKhoanDTO;

import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import com.javaweb.student_score_management.repository.GiangVienRepository;
import com.javaweb.student_score_management.repository.MonHocRepository;
import com.javaweb.student_score_management.repository.SinhVienRepository;
import com.javaweb.student_score_management.repository.TaiKhoanRepository;

import com.javaweb.student_score_management.entity.*;
import com.javaweb.student_score_management.repository.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaiKhoanService {
    private static final Logger logger = LoggerFactory.getLogger(TaiKhoanService.class);

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private DiemRepository diemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MonHocRepository monHocRepository;

    public TaiKhoanEntity findByUsername(String username) {
        return taiKhoanRepository.findByUsername(username).orElse(null);
    }

    public List<TaiKhoanDTO> getAllTaiKhoan() {
        List<TaiKhoanEntity> listTaiKhoan = taiKhoanRepository.findAll();
        return listTaiKhoan.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TaiKhoanDTO convertToDTO(TaiKhoanEntity taiKhoan) {
        String name = "";
        Integer maChuTK = null;
        String email = "";

        if (taiKhoan.getMaAdmin() != null) {
            name = taiKhoan.getMaAdmin().getTenAdmin();
            maChuTK = taiKhoan.getMaAdmin().getMaAdmin();
            email = taiKhoan.getMaAdmin().getEmail();
        } else if (taiKhoan.getMaGV() != null) {
            name = taiKhoan.getMaGV().getTenGV();
            maChuTK = taiKhoan.getMaGV().getMaGV();
            email = taiKhoan.getMaGV().getEmail();
        } else if (taiKhoan.getMaSV() != null) {
            name = taiKhoan.getMaSV().getTenSV();
            maChuTK = taiKhoan.getMaSV().getMaSV();
            email = taiKhoan.getMaSV().getEmail();
        }

        return new TaiKhoanDTO(
                taiKhoan.getRole().toString(),
                taiKhoan.getUsername(),
                taiKhoan.getPassword(),
                email,
                name,
                taiKhoan.getMaTK(),
                maChuTK
        );
    }
    public Boolean create(TaiKhoanDTO taiKhoan) {
        try {
            if (taiKhoanRepository.existsByUsername(taiKhoan.getUsername())) {
                logger.error("Username đã tồn tại: {}", taiKhoan.getUsername());
                return false;
            }


            TaiKhoanEntity taiKhoanEntity = new TaiKhoanEntity();
            if (taiKhoan.getAccount_type().equals("GiangVien")) {
                if (giangVienRepository.existsByEmail(taiKhoan.getEmail())) {
                    logger.error("Email đã tồn tại trong GiangVien: {}", taiKhoan.getEmail());
                    return false;
                }
                GiangVienEntity giangVienEntity = new GiangVienEntity();
                giangVienEntity.setEmail(taiKhoan.getEmail());
                giangVienEntity.setTenGV(taiKhoan.getName());
                giangVienRepository.save(giangVienEntity);
                taiKhoanEntity.setMaGV(giangVienRepository.findById(giangVienEntity.getMaGV()).get());
                taiKhoanEntity.setRole(TaiKhoanEntity.Role.GiangVien);

            } else{
                if (sinhVienRepository.existsByEmail(taiKhoan.getEmail())) {
                    logger.error("Email đã tồn tại trong SinhVien: {}", taiKhoan.getEmail());
                    return false;
                }
                taiKhoanEntity.setRole(TaiKhoanEntity.Role.SinhVien);
                SinhVienEntity sinhVienEntity = new SinhVienEntity();
                sinhVienEntity.setEmail(taiKhoan.getEmail());
                sinhVienEntity.setTenSV(taiKhoan.getName());
                sinhVienRepository.save(sinhVienEntity);
                taiKhoanEntity.setMaSV(sinhVienRepository.findById(sinhVienEntity.getMaSV()).get());

            }


            taiKhoanEntity.setUsername(taiKhoan.getUsername());
            taiKhoanEntity.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
//            taiKhoan.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
            taiKhoanRepository.save(taiKhoanEntity);

            logger.info("Tạo tài khoản thành công: {}", taiKhoan.getUsername());
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi tạo tài khoản: ", e);
            return false;
        }
    }

    public TaiKhoanEntity findById(Integer id) {
        return taiKhoanRepository.findById(id).orElse(null);
    }

    public Boolean update(TaiKhoanDTO taiKhoan) {
        try {
            Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(taiKhoan.getMaTK());
            if (optionalTaiKhoan.isPresent()) {

                TaiKhoanEntity taiKhoanEntity = optionalTaiKhoan.get();
                taiKhoanEntity.setUsername(taiKhoan.getUsername());
                taiKhoanEntity.getMaGV().setTenGV(taiKhoan.getName());

                if (taiKhoan.getPassword() != null && !taiKhoan.getPassword().isEmpty()) {
                    taiKhoanEntity.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
                }
                taiKhoanRepository.save(taiKhoanEntity);
                logger.info("Cập nhật tài khoản thành công: {}", taiKhoan.getUsername());
                return true;
            } else {
                logger.error("Không tìm thấy tài khoản với ID: {}", taiKhoan.getMaTK());
                return false;
            }
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật tài khoản", e);
            return false;
        }
    }

    public Boolean delete(Integer id) {
        try {
            if (!taiKhoanRepository.existsById(id)) {
                logger.error("Không tìm thấy tài khoản với ID: {}", id);
                return false;
            }

            Optional<TaiKhoanEntity> taiKhoanOpt = taiKhoanRepository.findById(id);
            if (taiKhoanOpt.isPresent()) {
                TaiKhoanEntity taiKhoan = taiKhoanOpt.get();

                // Kiểm tra nếu tài khoản có liên kết với giảng viên
                if (taiKhoan.getMaGV() != null) {
                    GiangVienEntity giangVien = taiKhoan.getMaGV();
                    List<MonHocEntity> danhSachMonHoc = monHocRepository.findByMaGV(giangVien);

                    if (!danhSachMonHoc.isEmpty()) {
                        logger.error("Không thể xóa giảng viên ID: {} vì còn môn học đang giảng dạy", id);
                        return false;
                    }
                }

                if (taiKhoan.getMaSV() != null) {
                    SinhVienEntity sinhVien = taiKhoan.getMaSV();
                    List<DiemEntity> danhSachDiem = diemRepository.findByMaSV(sinhVien);

                    if (!danhSachDiem.isEmpty()) {
                        logger.error("Không thể xóa sinh viên ID: {} vì đã có điểm môn học", id);
                        return false;
                    }
                }
              
                // Nếu không có ràng buộc, tiến hành xóa
                taiKhoanRepository.deleteById(id);
                logger.info("Xóa tài khoản thành công với ID: {}", id);
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error("Lỗi khi xóa tài khoản: ", e);
            return false;
        }
    }

}




