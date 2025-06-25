package com.example.appquanlycanhan.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;

public class TaskDataProvider {

    private SQLiteDatabase database;

    public TaskDataProvider(Context context) {
        // Khởi tạo cơ sở dữ liệu
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public int getCompletedTasksCount() {
        // Truy vấn số lượng nhiệm vụ hoàn thành (is_completed = 1)
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM NhiemVuDocLap WHERE is_completed = 1", null);
        cursor.moveToFirst();
        int completedTasks = cursor.getInt(0);
        cursor.close();
        return completedTasks;
    }

    public int getIncompleteTasksCount() {
        // Truy vấn số lượng nhiệm vụ chưa hoàn thành (is_completed = 0)
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM NhiemVuDocLap WHERE is_completed = 0", null);
        cursor.moveToFirst();
        int incompleteTasks = cursor.getInt(0);
        cursor.close();
        return incompleteTasks;
    }
}
