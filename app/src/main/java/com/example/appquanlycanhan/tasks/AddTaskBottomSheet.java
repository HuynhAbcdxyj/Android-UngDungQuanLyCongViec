package com.example.appquanlycanhan.tasks;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import com.example.appquanlycanhan.ThongBao.ReminderReceiver_Nv;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTaskBottomSheet extends DialogFragment implements AddTaskBottomSheetListener{

    private Button btnNhacNho , btnHoanTat;
    private EditText edtNhiemVu;
    private String ngayNhacNho = null;
    private String gioNhacNho = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout cho BottomSheet
        return inflater.inflate(R.layout.activity_add_task_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cài đặt hiệu ứng chuyển động từ dưới lên
        if (getDialog() != null) {
            getDialog().getWindow().setWindowAnimations(R.style.BottomSheetAnimation);
        }

        // Khai báo và cài đặt sự kiện cho nút nhắc nhở
        btnNhacNho = view.findViewById(R.id.btnNhacNho); // Sử dụng view.findViewById để tham chiếu nút từ layout của Fragment
        btnHoanTat = view.findViewById(R.id.btnHoanTat);
        edtNhiemVu = view.findViewById(R.id.edtNhiemVu);
        if (getArguments() != null){
            int id = getArguments().getInt("id");
            String tieuDe = getArguments().getString("tieu_de");
            String thoiGianNhacNho = getArguments().getString("thoi_gian_nhac_nho");

            edtNhiemVu.setText(tieuDe);
            btnNhacNho.setText(thoiGianNhacNho);
        }
        btnNhacNho.setOnClickListener(v -> {
            ReminderBottomSheet reminderBottomSheet = new ReminderBottomSheet();
            reminderBottomSheet.setTargetFragment(AddTaskBottomSheet.this, 0); // Đặt AddTaskBottomSheet làm target
            reminderBottomSheet.show(getParentFragmentManager(), "ReminderBottomSheet");
        });

        btnHoanTat.setOnClickListener(v -> {
            luuNhiemVu();
        });

    }

    @Override
    public void onDateTimeSelected(String date, String time) {
        ngayNhacNho = date;
        gioNhacNho = time;

        // Cập nhật text cho nút Nhắc Nhở
        btnNhacNho.setText(String.format("%s %s", ngayNhacNho, gioNhacNho));
    }

    @SuppressLint("ScheduleExactAlarm")
    private void luuNhiemVu() {
        String thoiGianNhacNho = btnNhacNho.getText().toString().trim();
        String tieuDeNhiemVu = edtNhiemVu.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (tieuDeNhiemVu.isEmpty()) {
            Toast.makeText(getContext(), "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (thoiGianNhacNho.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn thời gian nhắc nhở", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tieu_de", tieuDeNhiemVu);
        values.put("thoi_gian_nhac_nho", thoiGianNhacNho);

        long result;
        if (getArguments() != null && getArguments().containsKey("id")) {
            // Cập nhật nhiệm vụ
            int id = getArguments().getInt("id");
            result = db.update("NhiemVuDocLap", values, "id = ?", new String[]{String.valueOf(id)});
        } else {
            // Thêm nhiệm vụ mới
            result = db.insert("NhiemVuDocLap", null, values);
        }

        if (result != -1) {
            Toast.makeText(getContext(), "Nhiệm vụ đã được lưu!", Toast.LENGTH_SHORT).show();

            int taskId = (int) result; // Lấy ID của nhiệm vụ nếu thêm mới
            if (getArguments() != null && getArguments().containsKey("id")) {
                taskId = getArguments().getInt("id"); // Lấy ID nếu là nhiệm vụ đã tồn tại
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = dateFormat.parse(thoiGianNhacNho);
                long timeInMillis = date.getTime();
                setReminder(getActivity(), timeInMillis, tieuDeNhiemVu, taskId); // Truyền thêm taskId
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Lỗi khi lưu nhiệm vụ!", Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
    @SuppressLint("ScheduleExactAlarm")
    public void setReminder(Context context, long timeInMillis, String title, int taskId) {
        // Tạo Intent gửi đến ReminderReceiver
        Intent intent = new Intent(context, ReminderReceiver_Nv.class);
        intent.putExtra("titleNhiemVu", title);
        intent.putExtra("taskId", taskId); // Truyền thêm ID của nhiệm vụ để phân biệt

        // Tạo PendingIntent với ID duy nhất
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId, // Sử dụng taskId để tạo ID duy nhất cho PendingIntent
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Đặt lịch với AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }


}
