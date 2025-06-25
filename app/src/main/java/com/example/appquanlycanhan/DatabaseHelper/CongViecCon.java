package com.example.appquanlycanhan.DatabaseHelper;

public class CongViecCon {

    private int id;                  // ID công việc con
    private String tieuDe;           // Tiêu đề công việc con
    private String noiDung;          // Nội dung công việc con
    private String ngayTao;          // Ngày tạo công việc conở
    private String thoiGianNhacNho;
    private int idCha;

    public CongViecCon() {
    }

    // Constructor

    public CongViecCon(int id, String tieuDe, String noiDung, String ngayTao, String thoiGianNhacNho, int idCha) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.thoiGianNhacNho = thoiGianNhacNho;
        this.idCha = idCha;
    }

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

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }

    public void setThoiGianNhacNho(String thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }

    public int getIdCha() {
        return idCha;
    }

    public void setIdCha(int idCha) {
        this.idCha = idCha;
    }
}
