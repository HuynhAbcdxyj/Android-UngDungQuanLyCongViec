package com.example.appquanlycanhan.home;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appquanlycanhan.DatabaseHelper.CongViecCon;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import com.example.appquanlycanhan.ThongBao.ReminderReceiver;
import com.example.appquanlycanhan.tasks.ReminderBottomSheet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addHome extends AppCompatActivity {
    private EditText etTaskTitle, etTaskDescription;
    private TextView NgayGio, thoiGianNhacNho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_home);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        NgayGio = findViewById(R.id.NgayGio);
        thoiGianNhacNho = findViewById(R.id.thoiGianNhacNho);
        // nhan du lieu tu Intent
        Intent intent = getIntent();
        String tieuDe = intent.getStringExtra("tieuDe");
        // kiem tra xem tieu de co null khong
        if (tieuDe != null) {
            // truy van cong viec theo tieu de
            CongViecCon congViec = getCongViecByTieuDe(tieuDe);
            if (congViec != null) {
                //hien thi du lieu len UL
                etTaskTitle.setText(congViec.getTieuDe());
                etTaskDescription.setText(congViec.getNoiDung());
                thoiGianNhacNho.setText("Nhắc nhở vào: " + congViec.getThoiGianNhacNho());
            } else {
                //neu khong tim thay cong viec
                Log.e("ERROR", "Không tìm thấy công việc với tiêu đề : " + tieuDe);
            }
        }
        String ngayTao = intent.getStringExtra("ngayTao");
        if (ngayTao != null) {
            NgayGio.setText(ngayTao);
        } else {
            String currentDateTime = getCurrentDateTime();
            NgayGio.setText(currentDateTime);
        }
        // Xử lý menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu_giaodien1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.luu) {
            luuCongViecCon();
            return true;
        } else if (id == R.id.chuyenTiep) {
            return true;
        } else if (id == R.id.NhacNho) {
            ReminderBottomSheet reminderBottomSheet = new ReminderBottomSheet();
            reminderBottomSheet.show(getSupportFragmentManager(), "ReminderBottomSheet");
            return true;
        } else if (id == android.R.id.home) {
            // Xử lý nút quay lại
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phương thức nhận thời gian nhắc nhở và cập nhật thoiGianNhacNho
    public void onReminderTimeSelected(String date, String time) {
        // Kết hợp ngày và giờ thành một chuỗi
        String reminderDateTime = time + " " + date;
        // Cập nhật TextView thoiGianNhacNho
        thoiGianNhacNho.setText(reminderDateTime);
    }
    private void luuCongViecCon() {
        // Lấy dữ liệu từ giao diện
        String tieuDe = etTaskTitle.getText().toString().trim();
        String noiDung = etTaskDescription.getText().toString().trim();
        String ngayTao = getCurrentDateTime(); // Lấy ngày giờ hiện tại
        String thoigianNhacNho = thoiGianNhacNho.getText().toString().trim();

        // Kiểm tra dữ liệu có hợp lệ không
        if (tieuDe.isEmpty()) {
            Toast.makeText(this, "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (thoigianNhacNho.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn thời gian nhắc nhở", Toast.LENGTH_SHORT).show();
            return;
        }

        int idcha = getIntent().getIntExtra("parentId", -1);

        // Tạo kết nối tới cơ sở dữ liệu
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Kiểm tra nếu đang sửa công việc cũ (có tieuDeOld)
        Intent intent = getIntent();
        String tieuDeOld = intent.getStringExtra("tieuDe"); // Nhận tiêu đề cũ từ intent

        ContentValues values = new ContentValues();
        values.put("tieu_de", tieuDe);     // Lưu tiêu đề
        values.put("noi_dung", noiDung);  // Lưu nội dung
        values.put("ngay_tao", ngayTao);  // Lưu ngày giờ tạo
        values.put("thoi_gian_nhac_nho", thoigianNhacNho);  // Lưu thời gian nhắc nhở

        if (idcha != -1) {
            values.put("id_cha", idcha); // Lưu id cha
        }

        if (tieuDeOld != null && !tieuDeOld.isEmpty()) {
            // Cập nhật công việc cũ (update `trang_thai` về 0 nếu sửa đổi thời gian nhắc nhở)
            values.put("trang_thai", 0); // Đặt trạng thái về 0
            int result = db.update("CongViecCon", values, "tieu_de = ?", new String[]{tieuDeOld});
            if (result > 0) {
                Toast.makeText(this, "Cập nhật công việc thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật công việc thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Thêm mới công việc
            values.put("trang_thai", 0); // Đặt trạng thái ban đầu là 0
            long result = db.insert("CongViecCon", null, values);
            if (result != -1) {
                Toast.makeText(this, "Lưu công việc thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lưu công việc thất bại!", Toast.LENGTH_SHORT).show();
            }
        }

        // Gọi hàm setReminder
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = dateFormat.parse(thoigianNhacNho); // Chuyển thời gian nhắc nhở thành Date
            long timeInMillis = date.getTime(); // Chuyển thành mili giây
            setReminder(this, timeInMillis, tieuDe); // Đặt nhắc nhở
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideKeyboard();
    }

    @SuppressLint("ScheduleExactAlarm")
    public void setReminder(Context context, long timeInMillis, String title) {
        // Sử dụng một ID duy nhất cho mỗi công việc, có thể sử dụng title hoặc thời gian nhắc nhở
        int jobId = (int) (timeInMillis % Integer.MAX_VALUE); // Sử dụng timeInMillis làm base để tạo jobId
        // Tạo Intent gửi đến ReminderReceiver
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("title", title); // Truyền tiêu đề công việc
        intent.putExtra("jobId", jobId); // Truyền ID công việc

        // Tạo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                jobId, // Sử dụng jobId làm ID để phân biệt các công việc
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Đặt lịch với AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    // Hàm lấy ngày giờ hiện tại theo định dạng
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    @SuppressLint("Range")
    private CongViecCon getCongViecByTieuDe(String tieuDe) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM CongViecCon WHERE tieu_de = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tieuDe});
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy thông tin công việc từ cursor
            CongViecCon congViec = new CongViecCon();
            congViec.setId(cursor.getInt(cursor.getColumnIndex("id")));
            congViec.setTieuDe(cursor.getString(cursor.getColumnIndex("tieu_de")));
            congViec.setNoiDung(cursor.getString(cursor.getColumnIndex("noi_dung")));
            congViec.setNgayTao(cursor.getString(cursor.getColumnIndex("ngay_tao")));
            // Thêm thời gian nhắc nhở
            congViec.setThoiGianNhacNho(cursor.getString(cursor.getColumnIndex("thoi_gian_nhac_nho")));
            cursor.close();
            db.close();
            return congViec;
        }
        cursor.close();
        db.close();
        return null;
    }
    // Hàm ẩn bàn phím
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etTaskTitle.getWindowToken(), 0);
        }
        etTaskTitle.clearFocus();
        etTaskDescription.clearFocus();
    }
}
