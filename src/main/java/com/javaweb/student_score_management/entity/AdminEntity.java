package com.javaweb.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "admin")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maAdmin;

    @Column(name = "tenAdmin")
    private String tenAdmin;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "maAdmin")
    @JsonManagedReference("admin-taiKhoan")
    private List<TaiKhoanEntity> listTaiKhoan;

    public Integer getMaAdmin() {
        return maAdmin;
    }

    public void setMaAdmin(Integer maAdmin) {
        this.maAdmin = maAdmin;
    }

    public String getTenAdmin() {
        return tenAdmin;
    }

    public void setTenAdmin(String tenAdmin) {
        this.tenAdmin = tenAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TaiKhoanEntity> getListTaiKhoan() {
        return listTaiKhoan;
    }

    public void setListTaiKhoan(List<TaiKhoanEntity> listTaiKhoan) {
        this.listTaiKhoan = listTaiKhoan;
    }
}
