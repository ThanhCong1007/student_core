package com.javaweb.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "taikhoan")
public class TaiKhoanEntity {

    public enum Role {
        Admin, GiangVien, SinhVien
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTK;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "maAdmin")
    @JsonBackReference("admin-taiKhoan")
    private AdminEntity maAdmin;

    @ManyToOne
    @JoinColumn(name = "maGV")
    @JsonBackReference("giangvien-taiKhoan")
    private GiangVienEntity maGV;

    @ManyToOne
    @JoinColumn(name = "maSV")
    @JsonBackReference("sinhvien-taiKhoan")
    private SinhVienEntity maSV;

    public TaiKhoanEntity() {
    }

    public Integer getMaTK() {
        return maTK;
    }

    public void setMaTK(Integer maTK) {
        this.maTK = maTK;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AdminEntity getMaAdmin() {
        return maAdmin;
    }

    public void setMaAdmin(AdminEntity maAdmin) {
        this.maAdmin = maAdmin;
    }

    public GiangVienEntity getMaGV() {
        return maGV;
    }

    public void setMaGV(GiangVienEntity maGV) {
        this.maGV = maGV;
    }

    public SinhVienEntity getMaSV() {
        return maSV;
    }

    public void setMaSV(SinhVienEntity maSV) {
        this.maSV = maSV;
    }
}