package com.javaweb.student_score_management.controller;

import com.javaweb.student_score_management.DTO.TaiKhoanDTO;
import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import com.javaweb.student_score_management.repository.TaiKhoanRepository;
import com.javaweb.student_score_management.service.implement.CustomUserDetails;
import com.javaweb.student_score_management.service.implement.TaiKhoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepository taiKhoanRepository;

    public AdminController(TaiKhoanService taiKhoanService, TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanService = taiKhoanService;
        this.taiKhoanRepository = taiKhoanRepository;
    }
    //Minh Anh vippro 2k3
    // Trang admin
    @GetMapping("/index")
    public String adminIndex(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("taiKhoan", userDetails);
        return "admin/index";
    }

    // API lấy danh sách tài khoản (JSON)
    @GetMapping("/listTaiKhoan")
    @ResponseBody
    public ResponseEntity<List<TaiKhoanDTO>> getListTaiKhoan() {
        return ResponseEntity.ok(taiKhoanService.getAllTaiKhoan());
    }

//    @GetMapping("/index/listTaiKhoan")
//    public String listTaiKhoan() {
//        return "admin/index/listTaiKhoan/index";
//    }
    // API thêm tài khoản (JSON)
    @PostMapping("/addTaiKhoan")
    public ResponseEntity<Map<String, String>> addAccount(@RequestBody TaiKhoanDTO taiKhoan) {
        logger.info("Dữ liệu nhận được: {}", taiKhoan);

        Map<String, String> response = new HashMap<>();

        if (taiKhoan.getUsername() == null || taiKhoan.getUsername().trim().isEmpty()) {
            response.put("message", "Username không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        if (taiKhoan.getPassword() == null || taiKhoan.getPassword().trim().isEmpty()) {
            response.put("message", "Password không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        boolean created = taiKhoanService.create(taiKhoan);
        if (created) {
            response.put("message", "Tạo tài khoản thành công!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Tạo tài khoản thất bại!");
            return ResponseEntity.badRequest().body(response);
        }
    }
    // API cập nhật tài khoản (JSON)
    @PutMapping("/editTaiKhoan/{id}")
    @ResponseBody
    public ResponseEntity<String> updateAccount(@PathVariable Integer id, @RequestBody TaiKhoanDTO taiKhoan) {
        taiKhoan.setMaTK(id);

        if (taiKhoanService.update(taiKhoan)) {
            return ResponseEntity.ok("{\"message\": \"Cập nhật tài khoản thành công!\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Cập nhật tài khoản thất bại!\"}");
        }
    }

    // Xóa tài khoản (JSON)
    @DeleteMapping("/deleteTaiKhoan/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteAccount(@PathVariable Integer id) {
        boolean deleted = taiKhoanService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Xóa tài khoản thành công!");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy tài khoản với ID: " + id);
        }
    }

// Thymeleaf API
    @GetMapping("/index/listTaiKhoan")
    public String listTaiKhoan() {
        return "admin/index/listTaiKhoan/index";
    }

    @GetMapping("/addTaiKhoan")
    public String addTaiKhoan() {
        return "admin/index/listTaiKhoan/add-TaiKhoan";
    }

    @GetMapping("/editTaiKhoan/{id}")
    public String editTaiKhoan() {
        return "admin/index/listTaiKhoan/edit-TaiKhoan";
    }

    // Văn Huy =================================================================================

//    // Hiển thị danh sách tài khoản (dùng View) trả về 1 trang html
//    @GetMapping("/index/listTaiKhoan")
//    public String listAccounts(Model model) {
//        List<TaiKhoanEntity> list = taiKhoanService.getAllTaiKhoan();
//        model.addAttribute("list", list);
//        return "admin/index/listTaiKhoan/index";
//    }
//    // Form thêm tài khoản (View) trả về 1 trang html
//    @GetMapping("/addTaiKhoan")
//    public String showAddForm(Model model) {
//        model.addAttribute("dstk", new TaiKhoanEntity());
//        return "admin/index/listTaiKhoan/add-TaiKhoan";
//    }
//
//    // Form chỉnh sửa tài khoản (View) trả về html
//    @GetMapping("/editTaiKhoan/{id}")
//    public String showEditForm(@PathVariable Integer id, Model model) {
//        TaiKhoanEntity taiKhoan = taiKhoanService.findById(id);
//        if (taiKhoan == null) {
//            throw new IllegalArgumentException("Invalid account ID: " + id);
//        }
//        model.addAttribute("dstk", taiKhoan);
//        return "admin/index/listTaiKhoan/edit-TaiKhoan";
//    }

}
