package com.example.appquanlycanhan.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyCongViec.db";
    private static final int DATABASE_VERSION = 3;
    private static DatabaseHelper instance;
    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Singleton getInstance method
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng công việc cha
        db.execSQL("CREATE TABLE IF NOT EXISTS CongViecCha (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tieu_de TEXT NOT NULL, " +  // Tiêu đề Tiếng Việt
                "ngay_tao TEXT NOT NULL)");

        // Bảng công việc con
        db.execSQL("CREATE TABLE IF NOT EXISTS CongViecCon (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tieu_de TEXT NOT NULL, " +  // Tiêu đề Tiếng Việt
                "noi_dung TEXT, " +
                "ngay_tao TEXT NOT NULL, " +
                "thoi_gian_nhac_nho TEXT, " +
                "id_cha INTEGER, " +           // Cho phép NULL
                "trang_thai INTEGER DEFAULT 0, " + // 0: Chưa hoàn thành, 1: Hoàn thành
                "FOREIGN KEY (id_cha) REFERENCES CongViecCha(id) ON DELETE CASCADE)");

        // Bảng nhiệm vụ độc lập
        db.execSQL("CREATE TABLE IF NOT EXISTS NhiemVuDocLap (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tieu_de TEXT, " +  // Tiêu đề Tiếng Việt
                "thoi_gian_nhac_nho TEXT, " +
                "is_completed INTEGER DEFAULT 0)"); // Thêm cột is_completed

        // Tạo bảng NguoiDung
        db.execSQL("CREATE TABLE IF NOT EXISTS NguoiDung (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ho_ten TEXT NOT NULL, " +
                "mat_khau TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CongViecCha");
        db.execSQL("DROP TABLE IF EXISTS CongViecCon");
        db.execSQL("DROP TABLE IF EXISTS NhiemVuDocLap");
        db.execSQL("DROP TABLE IF EXISTS NguoiDung");
        onCreate(db);
    }

    // Thêm người dùng
    public boolean addUser(String username, String password) {
        if (isUserExist(username)) {
            return false; // Trả về false nếu tên người dùng đã tồn tại
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ho_ten", username.trim());
        values.put("mat_khau", password.trim());

        long result = db.insert("NguoiDung", null, values);
        return result != -1;
    }

    // Kiểm tra người dùng tồn tại hay không
    public boolean isUserExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM NguoiDung WHERE ho_ten = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username.trim()});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Kiểm tra người dùng và mật khẩu
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM NguoiDung WHERE ho_ten = ? AND mat_khau = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username.trim(), password.trim()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
//lấy tất cả công việc con
    @SuppressLint("Range")
    public List<CongViecCon> getAllTasks() {
        List<CongViecCon> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CongViecCon", null);
        if (cursor.moveToFirst()) {
            do {
                CongViecCon task = new CongViecCon();
                task.setTieuDe(cursor.getString(cursor.getColumnIndex("tieu_de")));
                task.setNgayTao(cursor.getString(cursor.getColumnIndex("ngay_tao")));
                task.setThoiGianNhacNho(cursor.getString(cursor.getColumnIndex("thoi_gian_nhac_nho")));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }
    // lấy công việc con theo id cha
    @SuppressLint("Range")
    public List<CongViecCon> getTasksByParentId(int idCha) {
        List<CongViecCon> congViecConList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CongViecCon WHERE id_cha = ?", new String[]{String.valueOf(idCha)});

        if (cursor.moveToFirst()) {
            do {
                CongViecCon congViecCon = new CongViecCon();
                congViecCon.setId(cursor.getInt(cursor.getColumnIndex("id")));
                congViecCon.setTieuDe(cursor.getString(cursor.getColumnIndex("tieu_de")));
                congViecCon.setNoiDung(cursor.getString(cursor.getColumnIndex("noi_dung")));
                congViecCon.setNgayTao(cursor.getString(cursor.getColumnIndex("ngay_tao")));
                congViecCon.setThoiGianNhacNho(cursor.getString(cursor.getColumnIndex("thoi_gian_nhac_nho")));
                congViecCon.setIdCha(cursor.getInt(cursor.getColumnIndex("id_cha")));
                congViecConList.add(congViecCon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return congViecConList;
    }
    // lấy tất cả nhiệm vụ độc lập
    @SuppressLint("Range")
    public List<NhiemVuDocLap> getAllNhiemVuDocLap() {
        List<NhiemVuDocLap> nhiemVuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NhiemVuDocLap", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tieuDe = cursor.getString(cursor.getColumnIndex("tieu_de"));
                String thoiGianNhacNho = cursor.getString(cursor.getColumnIndex("thoi_gian_nhac_nho"));
                int isCompletedInt = cursor.getInt(cursor.getColumnIndex("is_completed"));
                boolean isCompleted = isCompletedInt == 1;  // Chuyển đổi giá trị 0/1 thành boolean
                nhiemVuList.add(new NhiemVuDocLap(id, tieuDe, thoiGianNhacNho, isCompleted));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nhiemVuList;
    }
    // Thêm công việc cha vào cơ sở dữ liệu
    public long addCongViecCha(String tieuDe, String ngayTao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tieu_de", tieuDe);
        values.put("ngay_tao", ngayTao);
        return db.insert("CongViecCha", null, values);
    }
    // Lấy tất cả công việc cha từ cơ sở dữ liệu
    @SuppressLint("Range")
    public List<CongViecCha> getAllCongViecCha() {
        List<CongViecCha> congViecChaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CongViecCha", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tieuDe = cursor.getString(cursor.getColumnIndex("tieu_de"));
                String ngayTao = cursor.getString(cursor.getColumnIndex("ngay_tao"));
                CongViecCha congViecCha = new CongViecCha(id, tieuDe, ngayTao);
                congViecChaList.add(congViecCha);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return congViecChaList;
    }
    // Cập nhật trạng thái nhiệm vụ
    public void updateTaskStatus(NhiemVuDocLap task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_completed", task.isCompleted() ? 1 : 0);
        db.update("NhiemVuDocLap", values, "id = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }
    // Xóa công việc cha
    public boolean deleteCongViecCha(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("CongViecCha", "id = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    // Xóa nhiệm vụ theo ID
    public boolean deleteTaskById(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("NhiemVuDocLap", "id = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }
// xóa công việc con
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CongViecCon", "id = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }
// lấy công việc con có cùng id cha và có trạng thái  1 đã hoàn thành
    @SuppressLint("Range")
    public int getCompletedTasksCount(int idCha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM CongViecCon WHERE id_cha = ? AND trang_thai = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idCha)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
// lấy công đếm tổng số công việc con có cung idcha
    public int getTotalTasksCount(int idCha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM CongViecCon WHERE id_cha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idCha)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    // Xoá tất cả dữ liệu trong cơ sở dữ liệu
    public boolean deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction(); // Bắt đầu giao dịch
            db.delete("CongViecCon", null, null);
            db.delete("CongViecCha", null, null);
            db.delete("NhiemVuDocLap", null, null);
            db.delete("NguoiDung", null, null);
            db.setTransactionSuccessful(); // Cam kết giao dịch
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error clearing database", e);
            return false;
        } finally {
            db.endTransaction(); // Kết thúc giao dịch
            db.close();
        }
    }

    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa người dùng dựa trên tên người dùng
        int rowsAffected = db.delete("NguoiDung", "ho_ten = ?", new String[]{username.trim()});
        db.close();
        return rowsAffected > 0;  // Trả về true nếu có ít nhất 1 hàng bị xóa
    }
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mat_khau", newPassword);

        int rows = db.update("NguoiDung", values, "ho_ten = ?", new String[]{username});
        return rows > 0;
    }




}
