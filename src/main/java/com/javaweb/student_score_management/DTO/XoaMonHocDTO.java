package com.javaweb.student_score_management.DTO;

import java.util.List;

public class XoaMonHocDTO {
    private Integer maSV;
    private List<Integer> maMHList;

    public Integer getMaSV() {
        return maSV;
    }

    public void setMaSV(Integer maSV) {
        this.maSV = maSV;
    }

    public List<Integer> getMaMHList() {
        return maMHList;
    }

    public void setMaMHList(List<Integer> maMHList) {
        this.maMHList = maMHList;
    }
}
