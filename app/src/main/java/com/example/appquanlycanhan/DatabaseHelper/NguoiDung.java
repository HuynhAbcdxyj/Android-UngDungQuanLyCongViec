package com.example.appquanlycanhan.DatabaseHelper;

public class NguoiDung {
    private String ho_ten;
    private String mat_khau;
    private int id;

    public NguoiDung() {
    }

    public NguoiDung(String ho_ten, String mat_khau, int id) {
        this.ho_ten = ho_ten;
        this.mat_khau = mat_khau;
        this.id = id;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
