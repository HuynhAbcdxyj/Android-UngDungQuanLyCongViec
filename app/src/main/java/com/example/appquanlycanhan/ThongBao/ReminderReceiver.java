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

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        int jobId = intent.getIntExtra("jobId", -1); // Lấy ID công việc từ Intent
        if (jobId == -1) {
            return; // Nếu không có ID hợp lệ, không làm gì
        }

        // Cập nhật trạng thái công việc trong cơ sở dữ liệu
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trang_thai", 1); // Đặt trạng thái là hoàn thành (1)

        // Cập nhật dựa trên tiêu đề công việc (hoặc ID nếu bạn lưu ID công việc trong Intent)
        int rowsUpdated = db.update("CongViecCon", values, "tieu_de = ?", new String[]{title});
        db.close();

        // Kiểm tra xem cập nhật có thành công không
        if (rowsUpdated > 0) {
            // Có thể thêm thông báo log hoặc toast nếu cần
        }

        // Tạo NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Tạo kênh thông báo (cho Android 8.0 trở lên)
        String channelId = "reminder_channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Nhắc nhở công việc",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }
        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_alarm_on_24) // Icon mặc định
                .setContentTitle("Nhắc nhở công việc")
                .setContentText("Công việc: " + title + " đã đến giờ nhắc nhở!") // Hiển thị tiêu đề công việc
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true); // Tự động hủy thông báo khi nhấn

        // Gửi thông báo với jobId làm ID để phân biệt các thông báo
        notificationManager.notify(jobId, builder.build());
    }
}

