package com.javaweb.student_score_management.controller;

import com.javaweb.student_score_management.DTO.DiemDTO;
import com.javaweb.student_score_management.entity.DiemEntity;
import com.javaweb.student_score_management.service.implement.DiemService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@RestController
//public class DiemController {
//    @Autowired
//    private DiemService diemService;
//
//    @GetMapping("/sinhvien/bangdiem")
//    public ResponseEntity<?> xemBangDiem(@RequestParam("maSV") Integer maSV) {
//        try {
//            List<DiemEntity> diemList = diemService.getDiembySinhVienID(maSV);
//            return ResponseEntity.ok(diemList);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//}
@Controller
@RequestMapping
public class DiemController {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DiemController.class);

    @Autowired
    private DiemService diemService;

    public DiemController(DiemService diemService) {
        this.diemService = diemService;
    }
    //Sinh Vien

    @GetMapping("/sinhvien/bangdiem")
    public String xemBangDiem(@RequestParam("maSV") Integer maSV, Model model) {
        List<DiemEntity> diemList = diemService.getDiemDetailsBySinhVienID(maSV);
        model.addAttribute("diemList", diemList);
        return "bangdiem";
    }

    //Admin chỉ có quyền xem điểm
    // API lấy danh sách điểm (JSON)
    @GetMapping("/admin/listDiem")
    @ResponseBody
    public ResponseEntity<List<DiemDTO>> getAllDiem() {
        return ResponseEntity.ok(diemService.getAllDiem());
    }

    //API lấy danh sách điểm (JSON) theo mã sv
    @GetMapping("/admin/findDiemByMaSV/{maSV}")
    @ResponseBody
    public ResponseEntity<List<DiemDTO>> getDiemByMaSV(@PathVariable Integer maSV) {
        return ResponseEntity.ok(diemService.getDiemByMaSV(maSV));
    }

    @GetMapping("/admin/diem")
    public String diem() {
        return "admin/diem/index";
    }
//    @DeleteMapping("/admin/diem/{id}")
//    public ResponseEntity<String> deleteDiem(@PathVariable Integer id) {
//        logger.info("Xóa điểm ID: {}", id);
//        boolean deleted = diemService.deleteDiem(id);
//        if (deleted) {
//            return ResponseEntity.ok("Xóa điểm thành công!");
//        } else {
//            return ResponseEntity.badRequest().body("Không tìm thấy điểm!");
//        }
//    }

//// Văn Huy =============================================================================
//    @GetMapping("/admin/diem")
//    public String listDiem(Model model) {
//        List<DiemDTO> diemList = diemService.getAllDiem();
//        model.addAttribute("diemList", diemList);
//        return "admin/diem/index"; // Trả về template index.html
//    }
//=======================================================================================
    //Giảng viên


    @PostMapping("/giangvien/diem")
    public ResponseEntity<String> addDiem(@RequestBody DiemDTO diemDTO) {
        logger.info("Dữ liệu điểm nhận được: {}", diemDTO);
        boolean created = diemService.createDiem(diemDTO);
        if (created) {
            return ResponseEntity.ok("Thêm điểm thành công!");
        } else {
            return ResponseEntity.badRequest().body("Thêm điểm thất bại!");
        }
    }

    @PutMapping("/giangvien/diem/{id}")
    public ResponseEntity<String> updateDiem(@PathVariable Integer id, @RequestBody DiemDTO diemDTO) {
        logger.info("Cập nhật điểm ID {} với dữ liệu: {}", id, diemDTO);
        boolean updated = diemService.updateDiem(id, diemDTO);
        if (updated) {
            return ResponseEntity.ok("Cập nhật điểm thành công!");
        } else {
            return ResponseEntity.badRequest().body("Cập nhật điểm thất bại!");
        }
    }

}