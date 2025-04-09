package com.javaweb.student_score_management.controller;

import com.javaweb.student_score_management.DTO.DangKyMonHocDTO;
import com.javaweb.student_score_management.DTO.DiemDTO;
import com.javaweb.student_score_management.DTO.MonHocDTO;
import com.javaweb.student_score_management.DTO.XoaMonHocDTO;
import com.javaweb.student_score_management.entity.DiemEntity;
import com.javaweb.student_score_management.service.implement.DiemService;
import com.javaweb.student_score_management.service.implement.MonHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class MonHocController {
    @Autowired
    private DiemService diemService;

    @Autowired
    private MonHocService monHocService;
    //Admin - Minh Anh

    // API lấy danh sách môn học (JSON)
    @GetMapping("/admin/listMonHoc")
    @ResponseBody
    public ResponseEntity<List<MonHocDTO>> getAllMonHoc() {
        return ResponseEntity.ok(monHocService.getAllMonHoc());
    }

    // API lấy môn học theo ID (JSON)
    @GetMapping("/admin/findMonHocByMaMH/{id}")
    public ResponseEntity<?> getMonHocById(@PathVariable Integer id) {
        MonHocDTO monHoc = monHocService.getMonHocById(id);
        if (monHoc != null) {
            return ResponseEntity.ok(monHoc);
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Không tìm thấy môn học với ID: " + id + "\"}");
        }
    }

    // API thêm môn học (JSON)
    @PostMapping("/admin/addMonHoc")
    public ResponseEntity<String> createMonHoc(@RequestBody MonHocDTO monHocDTO) {
        if (monHocDTO.getTenMH() == null || monHocDTO.getTenMH().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Tên môn học không được để trống\"}");
        }
        if (monHocDTO.getSoTinChi() == null || monHocDTO.getSoTinChi() <= 0) {
            return ResponseEntity.badRequest().body("{\"message\": \"Số tín chỉ phải lớn hơn 0\"}");
        }
        if (monHocDTO.getMaGV() == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Mã giảng viên không được để trống\"}");
        }
        if (monHocService.existsByTenMH(monHocDTO.getTenMH())) {
            return ResponseEntity.badRequest().body("{\"message\": \"Tên môn học đã tồn tại!\"}");
        }
        boolean created = monHocService.createMonHoc(monHocDTO);
        if (created) {
            return ResponseEntity.ok("{\"message\": \"Tạo môn học thành công!\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Tạo môn học thất bại!\"}");
        }
    }

    // API sửa môn học (JSON)
    @PutMapping("/admin/editMonHoc/{id}")
    @ResponseBody
    public ResponseEntity<String> updateMonHoc(@PathVariable Integer id, @RequestBody MonHocDTO monHocDTO) {
        try {
            monHocDTO.setMaMH(id);
            if (monHocService.updateMonHoc(id, monHocDTO)) {
                return ResponseEntity.ok("{\"message\": \"Cập nhật môn học thành công!\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Cập nhật môn học thất bại!\"}");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // API xóa môn học (JSON)
    @DeleteMapping("/admin/deleteMonHoc/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMonHoc(@PathVariable Integer id) {
        try {
            if (monHocService.deleteMonHoc(id)) {
                return ResponseEntity.ok("{\"message\": \"Xóa môn học thành công!\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Không tìm thấy môn học với ID: " + id + "\"}");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    //Thymeleaf API
    @GetMapping("/admin/monhoc")
    public String listMonHoc() {
        return "admin/monhoc/index";
    }

    @GetMapping("/admin/monhoc/add")
    public String addMonHoc() {
        return "admin/monhoc/addMonHoc";
    }

    @GetMapping("/admin/monhoc/edit/{id}")
    public String editMonHoc() {
        return "admin/monhoc/editMonHoc";
    }

    // Văn Huy ========================================================================

//    // Hiển thị danh sách môn học (View)
//    @GetMapping("/api/monhoc")
//    public String listMonHoc(Model model) {
//        List<MonHocDTO> list = monHocService.getAllMonHoc();
//        model.addAttribute("list", list);
//        return "admin/monhoc/index"; // templates/admin/monhoc/index.html
//    }
//
//    // Hiển thị form thêm môn học (View)
//    @GetMapping("/admin/monhoc/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("monHoc", new MonHocDTO());
//        return "admin/monhoc/addMonHoc"; // templates/admin/monhoc/addMonHoc.html
//    }
//
//    // Hiển thị form chỉnh sửa môn học (View)
//    @GetMapping("/admin/monhoc/edit/{id}")
//    public String showEditForm(@PathVariable Integer id, Model model) {
//        MonHocDTO monHoc = monHocService.getMonHocById(id);
//        if (monHoc == null) {
//            throw new IllegalArgumentException("Không tìm thấy môn học với ID: " + id);
//        }
//        model.addAttribute("monHoc", monHoc);
//        return "admin/monhoc/editMonHoc"; // templates/admin/monhoc/editMonHoc.html
//    }

    //  ===============================================================================


    // Sinh Viên
    @GetMapping("monhoc/search")
    public ResponseEntity<Map<String, Object>> searchMonHocByTenGanDung(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MonHocDTO> monHocList = monHocService.findMonHocByTenMHGanDung(keyword);
            response.put("monHocList", monHocList);
            response.put("message", "Tìm kiếm thành công");
            response.put("count", monHocList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi tìm kiếm: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/sinhvien/dangky")
    public ResponseEntity<Map<String, Object>> dangKyMonHoc(@RequestBody DangKyMonHocDTO dangKyMonHocDTO) {
        Map<String, Object> response = new HashMap<>();
        Integer maSV = dangKyMonHocDTO.getMaSV();
        List<Integer> maMHList = dangKyMonHocDTO.getMaMHList();
        List<Integer> monHocBiChan = new ArrayList<>();
        List<Integer> monHocDangKyThanhCong = new ArrayList<>();

        try {
            for (Integer maMH : maMHList) {
                List<DiemEntity> diemList = diemService.findByMaSVAndMaMH(maSV, maMH);

                // Nếu môn đã tồn tại và có điểm = null, thì không cho đăng ký lại
                boolean daCoDiemNull = diemList.stream().anyMatch(diem -> diem.getDiem() == null);
                if (!diemList.isEmpty() && daCoDiemNull) {
                    monHocBiChan.add(maMH);
                    continue; // Bỏ qua môn này
                }

                // Nếu môn hợp lệ, tiến hành đăng ký
                diemService.dangKyMonHoc(maSV, maMH);
                monHocDangKyThanhCong.add(maMH);
            }

            response.put("message", "Đăng ký môn học hoàn tất!");
            response.put("monHocBiChan", monHocBiChan);
            response.put("monHocDangKyThanhCong", monHocDangKyThanhCong);

            // Lấy danh sách điểm mới với DiemDTO
            List<DiemDTO> diemList = diemService.getDiemBySinhVienID(maSV);
            response.put("diemList", diemList);
            response.put("totalCourses", diemList.size());
        } catch (Exception e) {
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sinhvien/xoa")
    public ResponseEntity<Map<String, Object>> xoaNhieuMonHoc(@RequestBody XoaMonHocDTO xoaMonHocDTO) {
        Map<String, Object> response;
        try {
            response = diemService.xoaNhieuMonHoc(xoaMonHocDTO.getMaSV(), xoaMonHocDTO.getMaMHList());
        } catch (Exception e) {
            response = new HashMap<>();
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}
