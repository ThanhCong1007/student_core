package com.javaweb.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sinhvien")
public class SinhVienEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maSV;

    @Column(name = "tenSV")
    private String tenSV;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "maSV")
    @JsonManagedReference
    private List<DiemEntity> listDiem;

    @OneToMany(mappedBy = "maSV")
    private List<TaiKhoanEntity> listTaiKhoan;


    public Integer getMaSV() {
        return maSV;
    }

    public void setMaSV(Integer maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<DiemEntity> getListDiem() {
        return listDiem;
    }

    public void setListDiem(List<DiemEntity> listDiem) {
        this.listDiem = listDiem;
    }

    public List<TaiKhoanEntity> getListTaiKhoan() {
        return listTaiKhoan;
    }

    public void setListTaiKhoan(List<TaiKhoanEntity> listTaiKhoan) {
        this.listTaiKhoan = listTaiKhoan;
    }
}
