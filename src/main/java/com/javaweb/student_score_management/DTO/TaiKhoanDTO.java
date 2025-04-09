package com.javaweb.student_score_management.DTO;

public class TaiKhoanDTO {
    private String account_type;
    private String username;
    private String password;
    private String email;
    private String name;
    private int maTK;
    private Integer maChuTK;

    public TaiKhoanDTO(String account_type, String username, String password, String email, String name, int maTK, Integer maChuTK) {
        this.account_type = account_type;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.maTK = maTK;
        this.maChuTK = maChuTK;
    }

    public Integer getMaChuTK() {
        return maChuTK;
    }

    public void setMaChuTK(Integer maChuTK) {
        this.maChuTK = maChuTK;
    }



    public TaiKhoanDTO() { }

    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }


}
