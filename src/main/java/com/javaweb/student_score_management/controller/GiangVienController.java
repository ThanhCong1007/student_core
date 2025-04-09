package com.javaweb.student_score_management.controller;

import com.javaweb.student_score_management.DTO.DiemDTO;
import com.javaweb.student_score_management.DTO.MonHocDTO;
import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import com.javaweb.student_score_management.repository.GiangVienRepository;
import com.javaweb.student_score_management.repository.TaiKhoanRepository;
import com.javaweb.student_score_management.service.implement.*;
import com.javaweb.student_score_management.service.implement.ExcelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/giangvien")
public class GiangVienController {

    private static final Logger logger = LoggerFactory.getLogger(GiangVienController.class);

    @Autowired
    private MonHocService monHocService;

    @Autowired
    private DiemService diemService;

    @Autowired
    private ExcelService excelService;

//    @Autowired
//    private GiangVienService giangVienService;
//
//    @Autowired
//    private SinhVienService sinhVienService;
//
//    @Autowired
//    private TaiKhoanRepository taiKhoanRepository;

    // Trang chủ giảng viên
    @GetMapping("/index")
    public String giangVienIndex(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("taiKhoan", userDetails);
        return "giangvien/index";
    }

    // API render
    @GetMapping("/monhoc")
    public String renderGV_MH() {
        return "giangvien/monhoc";
    }

    @GetMapping("/sinhvien")
    public String renderGV_SV() {
        return "giangvien/sinhvien";
    }

    @GetMapping("/excel")
    public String renderGV_Excel() {
        return "giangvien/excel";
    }

    // Lấy MH mình dạy
    @GetMapping("/monhoc/{maGV}")
    public ResponseEntity<List<MonHocDTO>> getMonHocByMaGV(@PathVariable int maGV) {
        List<MonHocDTO> dsMonHoc = monHocService.getMonHocByMaGV(maGV);
        return ResponseEntity.ok(dsMonHoc);
    }
  
    // Lấy SV theo học môn mình dạy
    @GetMapping("/monhoc/{maGV}/{maMH}")
    public ResponseEntity<List<DiemDTO>> getSVByMaMHAndMaGV(@PathVariable int maMH, @PathVariable int maGV) {
        List<DiemDTO> dsDiem = diemService.getSV_Diem(maMH, maGV);
        return ResponseEntity.ok(dsDiem);
    }

    // Nhập điểm từ file Excel
    @PostMapping("/excel")
    public ResponseEntity<?> importGrades(@RequestParam(name = "file") MultipartFile file) {
        try {
            List<DiemDTO> importedGrades = excelService.importExcel(file);
            return ResponseEntity.ok(importedGrades);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi nhập file: " + e.getMessage());
        }
    }

//    // Trang danh sách môn học
//    @GetMapping("/monhoc")
//    public String monHocPage(Authentication authentication, Model model) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Integer maGV = userDetails.getMaGV();
//        if (maGV == null) {
//            model.addAttribute("error", "Không tìm thấy mã giảng viên!");
//            return "giangvien/monhoc";
//        }
//
//        List<MonHocDTO> dsMonHoc = monHocService.getMonHocByMaGV(maGV);
//        model.addAttribute("dsMonHoc", dsMonHoc);
//        model.addAttribute("maGV", maGV);
//        return "giangvien/monhoc";
//    }
//
//    // Trang quản lý sinh viên
//    @GetMapping("/monhoc/{maGV}/{maMH}")
//    public String sinhVienPage(@PathVariable int maGV, @PathVariable int maMH, Authentication authentication, Model model) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        if (userDetails.getMaGV() == null || userDetails.getMaGV() != maGV) {
//            model.addAttribute("error", "Bạn không có quyền truy cập danh sách này!");
//            return "giangvien/sinhvien";
//        }
//
//        model.addAttribute("maGV", maGV);
//        model.addAttribute("maMH", maMH);
//        model.addAttribute("sinhVien", new SinhVienEntity()); // For the add student form
//        return "giangvien/sinhvien";
//    }
//
//    // API để lấy danh sách sinh viên (JSON)
//    @GetMapping("/monhoc/{maGV}/{maMH}/sinhvien-list")
//    @ResponseBody
//    public ResponseEntity<List<DiemDTO>> getSinhVienList(@PathVariable int maGV, @PathVariable int maMH, Authentication authentication) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        if (userDetails.getMaGV() == null || userDetails.getMaGV() != maGV) {
//            return ResponseEntity.status(403).body(null); // Forbidden
//        }
//
//        List<DiemDTO> dsSinhVien = diemService.getSV_Diem(maMH, maGV);
//        return ResponseEntity.ok(dsSinhVien);
//    }
//
//
//
//    // API cập nhật điểm (JSON)
//    @PutMapping("/diem/{maGV}/{maMH}/{maSV}/update")
//    @ResponseBody
//    public ResponseEntity<String> updateDiem(@PathVariable int maGV, @PathVariable int maMH, @PathVariable int maSV, @RequestBody Map<String, Object> request, Authentication authentication) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        if (userDetails.getMaGV() == null || userDetails.getMaGV() != maGV) {
//            return ResponseEntity.status(403).body("{\"message\": \"Bạn không có quyền cập nhật điểm!\"}");
//        }
//
//        // Lấy dữ liệu từ request
//        Integer requestMaSV = (Integer) request.get("maSV");
//        Integer requestMaMH = (Integer) request.get("maMH");
//        Float diem = request.get("diem") != null ? ((Number) request.get("diem")).floatValue() : null;
//
//        // Kiểm tra xem maSV và maMH từ request có khớp với path variables không
//        if (requestMaSV == null || requestMaSV != maSV || requestMaMH == null || requestMaMH != maMH) {
//            return ResponseEntity.badRequest().body("{\"message\": \"Mã sinh viên hoặc mã môn học không khớp!\"}");
//        }
//
//        try {
//            diemService.updateDiem(maSV, maMH, diem);
//            return ResponseEntity.ok("{\"message\": \"Cập nhật điểm thành công!\"}");
//        } catch (Exception e) {
//            logger.error("Lỗi khi cập nhật điểm: {}", e.getMessage());
//            return ResponseEntity.badRequest().body("{\"message\": \"Cập nhật điểm thất bại: " + e.getMessage() + "\"}");
//        }
//    }
//
//    // Xử lý lỗi validation
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        StringBuilder errorMessage = new StringBuilder();
//        for (FieldError fieldError : fieldErrors) {
//            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
//        }
//        return ResponseEntity.badRequest().body("{\"message\": \"" + errorMessage.toString() + "\"}");
//    }
//
//    // Xóa sinh viên
//    @PostMapping("/monhoc/{maGV}/{maMH}/delete-sinhvien/{maSV}")
//    public String deleteSinhVien(@PathVariable int maGV, @PathVariable int maMH, @PathVariable int maSV, RedirectAttributes redirectAttributes) {
//        try {
//            sinhVienService.deleteSinhVien(maSV);
//            redirectAttributes.addFlashAttribute("success", "Xóa sinh viên thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sinh viên: " + e.getMessage());
//        }
//        return "redirect:/giangvien/monhoc/" + maGV + "/" + maMH;
//    }
//
//    // Trang bảng điểm
//    @GetMapping("/diem/{maGV}/{maMH}/{maSV}")
//    public String diemPage(@PathVariable int maGV, @PathVariable int maMH, @PathVariable int maSV, Authentication authentication, Model model) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        if (userDetails.getMaGV() == null || userDetails.getMaGV() != maGV) {
//            model.addAttribute("error", "Bạn không có quyền truy cập bảng điểm này!");
//            return "giangvien/diem";
//        }
//
//        DiemDTO diem = diemService.getDiemByMaSV(maMH, maGV, maSV);
//        model.addAttribute("diem", diem);
//        model.addAttribute("maGV", maGV);
//        model.addAttribute("maMH", maMH);
//        return "giangvien/diem";
//    }
//
//    // Redirect for invalid /giangvien/diem URL
//    @GetMapping("/diem")
//    public String redirectDiem(RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("error", "Vui lòng chọn môn học và sinh viên trước khi xem bảng điểm!");
//        return "redirect:/giangvien/monhoc";
//    }
//
//    // Redirect for invalid /giangvien/sinhvien URL
//    @GetMapping("/sinhvien")
//    public String redirectSinhVien(RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("error", "Vui lòng chọn môn học trước khi xem danh sách sinh viên!");
//        return "redirect:/giangvien/monhoc";
//    }
//
//    // Trang nhập điểm từ file Excel (hiển thị form upload)
//    @GetMapping("/excel")
//    public String showImportExcelForm(Authentication authentication, Model model) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        model.addAttribute("taiKhoan", userDetails);
//        return "giangvien/excel";
//    }
//
//    // Nhập điểm từ file Excel (xử lý file upload)
//    @PostMapping("/excel")
//    @ResponseBody
//    public ResponseEntity<?> importGrades(@RequestParam(name = "file") MultipartFile file) {
//        try {
//            List<DiemDTO> importedGrades = excelService.importExcel(file);
//            return ResponseEntity.ok(importedGrades);
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("Lỗi nhập file: " + e.getMessage());
//        }
//    }
//
//    // Xử lý cập nhật thông tin giảng viên (JSON)
//    @PostMapping("/update")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> updateGiangVien(Authentication authentication,
//                                                               @RequestBody Map<String, Object> requestData) {
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//
//        // Lấy dữ liệu từ JSON
//        Integer maGV = (Integer) requestData.get("maGV");
//        String tenGV = (String) requestData.get("tenGV");
//        String email = (String) requestData.get("email");
//
//        // Kiểm tra xem maGV từ request có khớp với maGV của người dùng hiện tại không
//        if (userDetails.getMaGV() != null && userDetails.getMaGV().equals(maGV)) {
//            GiangVienEntity giangVien = giangVienService.findByMaGV(maGV);
//            if (giangVien != null) {
//                giangVien.setTenGV(tenGV);
//                giangVien.setEmail(email);
//                giangVienService.save(giangVien);
//
//                // Cập nhật SecurityContext với thông tin mới
//                TaiKhoanEntity taiKhoan = taiKhoanRepository.findByUsername(userDetails.getUsername())
//                        .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));
//                UserDetails updatedUserDetails = new CustomUserDetails(taiKhoan);
//                Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
//                        updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//
//                return ResponseEntity.ok(Map.of("message", "Cập nhật thông tin thành công!"));
//            } else {
//                return ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy giảng viên với mã: " + maGV));
//            }
//        } else {
//            return ResponseEntity.badRequest().body(Map.of("message", "Bạn không có quyền cập nhật thông tin này!"));
//        }
//    }
}