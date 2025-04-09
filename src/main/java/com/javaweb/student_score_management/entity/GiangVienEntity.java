package com.javaweb.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "giangvien")
public class GiangVienEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maGV;

    @Column(name = "tenGV")
    private String tenGV;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "maGV")
    private List<MonHocEntity> listMonHoc;

    @OneToMany(mappedBy = "maGV")
    @JsonManagedReference
    private List<TaiKhoanEntity> listTaiKhoan;


    public Integer getMaGV() {
        return maGV;
    }

    public void setMaGV(Integer maGV) {
        this.maGV = maGV;
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MonHocEntity> getListMonHoc() {
        return listMonHoc;
    }

    public void setListMonHoc(List<MonHocEntity> listMonHoc) {
        this.listMonHoc = listMonHoc;
    }

    public List<TaiKhoanEntity> getListTaiKhoan() {
        return listTaiKhoan;
    }

    public void setListTaiKhoan(List<TaiKhoanEntity> listTaiKhoan) {
        this.listTaiKhoan = listTaiKhoan;
    }
}
