package com.javaweb.student_score_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.student_score_management.DTO.DiemDTO;
import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import com.javaweb.student_score_management.repository.SinhVienRepository;
import com.javaweb.student_score_management.service.implement.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/sinhvien")
public class SinhVienController {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @GetMapping("/index")
    public String sinhVienIndex(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.getRole() != TaiKhoanEntity.Role.SinhVien) {
            model.addAttribute("error", "Bạn không có quyền truy cập trang này!");
            return "sinhvien/index";
        }
        model.addAttribute("taiKhoan", userDetails);
        return "sinhvien/index";
    }
    @GetMapping("/dangkimonhoc")
    public String dangKyMonHoc(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.getRole() != TaiKhoanEntity.Role.SinhVien) {
            model.addAttribute("error", "Bạn không có quyền truy cập trang này!");
            return "sinhvien/dangkimonhoc";
        }
        if (userDetails.getMaSV() == null) {
            model.addAttribute("error", "Tài khoản của bạn không được liên kết với sinh viên nào!");
            return "sinhvien/dangkimonhoc";
        }
        model.addAttribute("taiKhoan", userDetails);
        return "sinhvien/dangkimonhoc";
    }

    @GetMapping("/bangdiem/{maSV}")
    public String bangDiem(@PathVariable Integer maSV, Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.getRole() != TaiKhoanEntity.Role.SinhVien) {
            model.addAttribute("error", "Bạn không có quyền truy cập trang này!");
            return "sinhvien/bangdiem";
        }
        if (userDetails.getMaSV() == null || !userDetails.getMaSV().equals(maSV)) {
            model.addAttribute("error", "Bạn không có quyền truy cập bảng điểm này!");
            return "sinhvien/bangdiem";
        }

        model.addAttribute("maSV", maSV);
        return "sinhvien/bangdiem";
    }

    @GetMapping("/{maSV}")
    public void getDiemBySinhVien(@PathVariable Integer maSV, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.getRole() != TaiKhoanEntity.Role.SinhVien) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Bạn không có quyền truy cập dữ liệu này!");
            return;
        }
        if (userDetails.getMaSV() == null || !userDetails.getMaSV().equals(maSV)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Bạn không có quyền truy cập dữ liệu này!");
            return;
        }

        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV).orElse(null);
        if (sinhVien == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Sinh viên không tồn tại!");
            return;
        }

        List<DiemDTO> diemDTOList = sinhVien.getListDiem().stream().map(diem -> {
            MonHocEntity monHoc = diem.getMaMH();
            GiangVienEntity giangVien = monHoc.getMaGV();

            return new DiemDTO(
                    diem.getMaDiem(),
                    sinhVien.getMaSV(),
                    sinhVien.getTenSV(),
                    monHoc.getMaMH(),
                    monHoc.getTenMH(),
                    giangVien.getMaGV(),
                    giangVien != null ? giangVien.getTenGV() : "Chưa có",
                    monHoc.getSoTinChi(),
                    diem.getDiem()
            );
        }).toList();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new ObjectMapper().writeValueAsString(diemDTOList));
    }
}