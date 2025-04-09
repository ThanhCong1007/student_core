package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.entity.GiangVienEntity;
import com.javaweb.student_score_management.entity.MonHocEntity;
import com.javaweb.student_score_management.entity.SinhVienEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendScoreEmail(SinhVienEntity sinhVienEntity, MonHocEntity monHocEntity, float diem, GiangVienEntity giangVienEntity) {

        String subject = "Bảng điểm vừa được cật nhật!!!";

        // Nội dung email
        String body = "Thân gửi " + sinhVienEntity.getTenSV() +  ",\n\n"
                + "Thông tin môn học vừa được cập nhật điểm như sau:\n\n"
                + "- Tên môn học: " + monHocEntity.getTenMH() + "\n"
                + "- Số tín chỉ: " + monHocEntity.getSoTinChi() +  "\n"
                + "- Điểm: " + diem + "\n\n"
                + "Nếu sinh viên có thắt mắc gì xin liên hệ với giảng viên:\n\n"
                + "[Tên giảng viên:" + giangVienEntity.getTenGV() + "\n"
                + "[Email giảng viên:" + giangVienEntity.getEmail() + "\n\n"
                + "Trân trọng,\n";

        // Tạo email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sinhVienEntity.getEmail());
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("your-email@gmail.com");

        // Gửi email
        mailSender.send(message);
    }

}