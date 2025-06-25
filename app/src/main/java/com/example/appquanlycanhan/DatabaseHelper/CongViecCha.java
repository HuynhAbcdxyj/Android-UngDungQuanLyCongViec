package com.example.appquanlycanhan.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CongViecCha {

    private int id;                  // ID công việc cha
    private String tieuDe;           // Tiêu đề công việc cha
    private String ngayTao;          // Ngày tạo công việc cha
    private List<CongViecCon> congViecConList; // Danh sách công việc con

    // Constructor
    public CongViecCha(int id, String tieuDe, String ngayTao) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.ngayTao = ngayTao;
        this.congViecConList = new ArrayList<>();
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

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public List<CongViecCon> getCongViecConList() {
        return congViecConList;
    }

    public void setCongViecConList(List<CongViecCon> congViecConList) {
        this.congViecConList = congViecConList;
    }

    public void addCongViecCon(CongViecCon congViecCon) {
        this.congViecConList.add(congViecCon);
    }
}
