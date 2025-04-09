package com.javaweb.student_score_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "monhoc")
public class MonHocEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maMH;

    @Column(name = "tenMH")
    private String tenMH;

    @Column(name = "soTinChi")
    private Integer soTinChi;

    @ManyToOne
    @JoinColumn(name = "maGV")
    @JsonBackReference
    private GiangVienEntity maGV;

    @OneToMany(mappedBy = "maMH")
    private List<DiemEntity> listDiem;

    public Integer getMaMH() {
        return maMH;
    }

    public void setMaMH(Integer maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi;
    }

    public GiangVienEntity getMaGV() {
        return maGV;
    }

    public void setMaGV(GiangVienEntity maGV) {
        this.maGV = maGV;
    }

    public List<DiemEntity> getListDiem() {
        return listDiem;
    }

    public void setListDiem(List<DiemEntity> listDiem) {
        this.listDiem = listDiem;
    }
}
