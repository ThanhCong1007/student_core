package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.DTO.DiemDTO;
import com.javaweb.student_score_management.entity.DiemEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import com.javaweb.student_score_management.repository.DiemRepository;
import com.javaweb.student_score_management.repository.MonHocRepository;
import com.javaweb.student_score_management.repository.SinhVienRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiemService {
    private static final Logger logger = LoggerFactory.getLogger(DiemService.class);

    @Autowired
    private DiemRepository diemRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    public List<DiemEntity> getDiemDetailsBySinhVienID(Integer maSV) {
        return diemRepository.getDiemDetailsByMaSV(maSV);
    }

    public boolean existsByMaMH(Integer maMH) {
        return diemRepository.existsByMaMH_MaMH(maMH);
    }

    public List<DiemEntity> findByMaSVAndMaMH(Integer maSV, Integer maMH) {
        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        MonHocEntity monHoc = monHocRepository.findById(maMH)
                .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

        return diemRepository.findByMaSVAndMaMH(sinhVien, monHoc);
    }
    // Đăng ký môn học mới (luôn thêm bản ghi mới)
    public String dangKyMonHoc(Integer maSV, Integer maMH) {
        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV).orElse(null);
        MonHocEntity monHoc = monHocRepository.findById(maMH).orElse(null);

        if (sinhVien == null || monHoc == null) {
            return "Sinh viên hoặc môn học không tồn tại!";
        }

        DiemEntity diemMoi = new DiemEntity();
        diemMoi.setMaSV(sinhVien);
        diemMoi.setMaMH(monHoc);
        diemMoi.setDiem(null); // Chưa có điểm

        diemRepository.save(diemMoi);
        return "Đăng ký môn học thành công!";
    }

    // Xóa môn học (chỉ xóa nếu chưa có điểm)
    public Map<String, Object> xoaNhieuMonHoc(Integer maSV, List<Integer> maMHList) {
        Map<String, Object> response = new HashMap<>();
        List<Integer> danhSachDaXoa = new ArrayList<>();
        List<Integer> danhSachKhongTheXoa = new ArrayList<>();

        SinhVienEntity sv = sinhVienRepository.findById(maSV)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        for (Integer maMH : maMHList) {
            MonHocEntity mh = monHocRepository.findById(maMH)
                    .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

            // Tìm tất cả bản ghi điểm của sinh viên với môn học
            List<DiemEntity> diemList = diemRepository.findByMaSVAndMaMH(sv, mh);

            if (diemList.isEmpty()) {
                danhSachKhongTheXoa.add(maMH);
                continue; // Bỏ qua môn này và xử lý môn tiếp theo
            }

            // Lọc ra danh sách điểm chưa có giá trị
            List<DiemEntity> diemChuaCoDiem = diemList.stream()
                    .filter(diem -> diem.getDiem() == null)
                    .collect(Collectors.toList());

            if (diemChuaCoDiem.isEmpty()) {
                danhSachKhongTheXoa.add(maMH);
            } else {
                diemRepository.deleteAll(diemChuaCoDiem);
                danhSachDaXoa.add(maMH);
            }
        }

        response.put("message", "Hủy đăng ký môn học hoàn tất!");
        response.put("daXoa", danhSachDaXoa);
        response.put("khongTheXoa", danhSachKhongTheXoa);

        return response;
    }

    // Admin
    public List<DiemDTO> getAllDiem() {
        List<DiemEntity> diemEntities = diemRepository.findAll();
        return diemEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<DiemDTO> getDiemByMaSV(Integer maSV) {
        List<DiemEntity> list = diemRepository.findByMaSV_MaSV(maSV);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean createDiem(DiemDTO diemDTO) {
        try {
            SinhVienEntity sinhVien = sinhVienRepository.findById(diemDTO.getMaSV())
                    .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));
            MonHocEntity monHoc = monHocRepository.findById(diemDTO.getMaMH())
                    .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

            DiemEntity diemEntity = new DiemEntity();
            diemEntity.setMaSV(sinhVien);
            diemEntity.setMaMH(monHoc);
            diemEntity.setDiem(diemDTO.getDiem());

            diemRepository.save(diemEntity);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi thêm điểm", e);
            return false;
        }
    }

    public boolean updateDiem(Integer id, DiemDTO diemDTO) {
        try {
            DiemEntity diemEntity = diemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Điểm không tồn tại!"));

            diemEntity.setDiem(diemDTO.getDiem());
            diemRepository.save(diemEntity);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật điểm", e);
            return false;
        }
    }

    // Cập nhật điểm dựa trên maSV và maMH
    public void updateDiem(int maSV, int maMH, Float diem) {
        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        MonHocEntity monHoc = monHocRepository.findById(maMH)
                .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

        List<DiemEntity> diemList = diemRepository.findByMaSVAndMaMH(sinhVien, monHoc);
        if (diemList.isEmpty()) {
            throw new RuntimeException("Không tìm thấy điểm cho sinh viên với mã SV: " + maSV + " và mã MH: " + maMH);
        }

        // Lấy bản ghi đầu tiên (giả định chỉ có một bản ghi cho mỗi sinh viên và môn học)
        DiemEntity diemEntity = diemList.get(0);
        diemEntity.setDiem(diem);
        diemRepository.save(diemEntity);
    }

    public boolean deleteDiem(Integer id) {
        try {
            if (diemRepository.existsById(id)) {
                diemRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xóa điểm", e);
            return false;
        }
    }

    // DTO có chức năng là sao chép các trường thông tin bên ENTITY để chỉ hiện những thông tin cần thiết/cho phép để hiển thị
    public DiemDTO convertToDTO(DiemEntity diemEntity) {
        return new DiemDTO(
                diemEntity.getMaDiem(),
                diemEntity.getMaSV().getMaSV(),
                diemEntity.getMaSV().getTenSV(),
                diemEntity.getMaMH().getMaMH(),
                diemEntity.getMaMH().getTenMH(),
                diemEntity.getMaMH().getMaGV().getMaGV(),
                diemEntity.getMaMH().getMaGV().getTenGV(),
                diemEntity.getMaMH().getSoTinChi(),
                diemEntity.getDiem()
        );
    }

    // Phương thức lấy chi tiết điểm của sinh viên
    public List<DiemDTO> getDiemBySinhVienID(Integer maSV) {
        // Kiểm tra sự tồn tại của sinh viên
        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        // Lấy danh sách điểm và chuyển đổi sang DTO
        List<DiemEntity> danhSachDiem = diemRepository.findByMaSV(sinhVien);

        // Log thông tin để debug
        logger.info("Lấy danh sách điểm cho sinh viên {}: {} môn học", maSV, danhSachDiem.size());

        // Chuyển đổi sang DTO để trả về thông tin chi tiết
        return danhSachDiem.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy DSSV (Diem) theo MaGV va MaMH
    public List<DiemDTO> getSV_Diem(int maMH, int maGV) {
        MonHocEntity monHocEntity = monHocRepository.findById(maMH)
                .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

        List<DiemEntity> listD1 = diemRepository.findByMaMH(monHocEntity);

        List<DiemDTO> diemDTO = new ArrayList<>();

        for (DiemEntity diemEntity : listD1) {
            if (maGV == diemEntity.getMaMH().getMaGV().getMaGV()) {
                DiemDTO diemDTO1 = convertToDTO(diemEntity);
                diemDTO.add(diemDTO1);
            }
        }

        return diemDTO;
    }

    // Lấy điểm của một sinh viên cụ thể trong một môn học và giảng viên
    public DiemDTO getDiemByMaSV(int maMH, int maGV, int maSV) {
        SinhVienEntity sinhVien = sinhVienRepository.findById(maSV)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        MonHocEntity monHoc = monHocRepository.findById(maMH)
                .orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));

        // Kiểm tra xem môn học có thuộc giảng viên không
        if (monHoc.getMaGV() == null || monHoc.getMaGV().getMaGV() != maGV) {
            throw new RuntimeException("Môn học không thuộc giảng viên này!");
        }

        List<DiemEntity> diemList = diemRepository.findByMaSVAndMaMH(sinhVien, monHoc);
        if (diemList.isEmpty()) {
            return null; // Không có điểm
        }

        // Lấy bản ghi đầu tiên (giả định chỉ có một bản ghi cho mỗi sinh viên và môn học)
        DiemEntity diemEntity = diemList.get(0);
        return convertToDTO(diemEntity);
    }
}