package com.example.appquanlycanhan.ThongBao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;

import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;

public class ReminderReceiver_Nv extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("titleNhiemVu");
        int taskId = intent.getIntExtra("taskId", 0); // Nhận ID của nhiệm vụ

        // Tạo NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo kênh thông báo
        String channelId = "job_channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Nhắc nhở nhiệm vụ",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_alarm_on_24) // Icon mặc định
                .setContentTitle("Nhắc nhở nhiệm vụ")
                .setContentText("Nhiệm vụ: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Gửi thông báo với ID là taskId
        notificationManager.notify(taskId, builder.build());

        // Cập nhật trạng thái nhiệm vụ thành "đã hoàn thành"
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_completed", 1); // Đặt trạng thái là 1 (đã hoàn thành)

        int rowsAffected = db.update("NhiemVuDocLap", values, "id = ?", new String[]{String.valueOf(taskId)});
        if (rowsAffected > 0) {
            // Log hoặc xử lý nếu cần thiết
        }

        db.close();
    }

}
