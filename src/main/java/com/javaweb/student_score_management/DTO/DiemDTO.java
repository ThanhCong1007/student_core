package com.javaweb.student_score_management.DTO;

public class DiemDTO {
    private Integer maDiem;
    private Integer maSV;
    private String tenSV;
    private Integer maMH;
    private String tenMH;
    private Integer maGV;
    private String tenGV;
    private Integer soTinChi;
    private Float diem;

    public DiemDTO(Integer maDiem, Integer maSV, String tenSV, Integer maMH, String tenMH, Integer maGV, String tenGV, Integer soTinChi, Float diem) {
        this.maDiem = maDiem;
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.maGV = maGV;
        this.tenGV = tenGV;
        this.soTinChi = soTinChi;
        this.diem = diem;
    }

    public Integer getMaGV() {
        return maGV;
    }

    public void setMaGV(Integer maGV) {
        this.maGV = maGV;
    }

    public Integer getMaDiem() {
        return maDiem;
    }

    public void setMaDiem(Integer maDiem) {
        this.maDiem = maDiem;
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV;
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

    public Float getDiem() {
        return diem;
    }

    public void setDiem(Float diem) {
        this.diem = diem;
    }

    public Integer getMaSV() {
        return maSV;
    }

    public void setMaSV(Integer maSV) {
        this.maSV = maSV;
    }

    public Integer getMaMH() {
        return maMH;
    }

    public void setMaMH(Integer maMH) {
        this.maMH = maMH;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }
}
