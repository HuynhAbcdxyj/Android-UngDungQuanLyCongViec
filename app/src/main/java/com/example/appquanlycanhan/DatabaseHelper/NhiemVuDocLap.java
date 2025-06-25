package com.example.appquanlycanhan.DatabaseHelper;

public class NhiemVuDocLap {
    private int id;
    private String tieuDe;
    private String thoiGianNhacNho;
    private boolean isCompleted;

    public NhiemVuDocLap() {
    }

    // Constructor
    public NhiemVuDocLap(int id, String tieuDe, String thoiGianNhacNho, boolean isCompleted) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.thoiGianNhacNho = thoiGianNhacNho;
        this.isCompleted = isCompleted;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }

    public void setThoiGianNhacNho(String thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }
    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
