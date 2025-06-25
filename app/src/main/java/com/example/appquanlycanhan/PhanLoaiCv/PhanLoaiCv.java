package com.example.appquanlycanhan.PhanLoaiCv;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appquanlycanhan.DatabaseHelper.CongViecCha;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhanLoaiCv extends AppCompatActivity {

    private ListView listViewFolders;
    private TaskAdapter_PhanLoaiCv adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_loai_cv);
        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbarPhanLoaiCv);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Gắn adapter vào ListView
        listViewFolders = findViewById(R.id.listViewFolders);
        adapter = new TaskAdapter_PhanLoaiCv(this, R.layout.list_folder, new ArrayList<>());
        listViewFolders.setAdapter(adapter);
        Button btnAddTask = findViewById(R.id.addFolderButton);

        // Tải danh sách công việc từ cơ sở dữ liệu
        loadTasksFromDatabase();

        // Sự kiện nhấn nút thêm
        btnAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Sự kiện nhấn giữ để xóa (giữ nguyên)
        listViewFolders.setOnItemLongClickListener((parent, view, position, id) -> {
            CongViecCha selectedTask = adapter.getItem(position); // Lấy công việc được chọn
            if (selectedTask != null) {
                // Hiển thị hộp thoại xác nhận xóa
                showDeleteConfirmationDialog(selectedTask);
            }
            return true; // Return true để ngừng sự kiện tiếp theo
        });
    }
    // Hiển thị hộp thoại thêm công việc
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm công việc mới");

        // Tạo EditText để nhập tiêu đề công việc
        final EditText input = new EditText(this);
        input.setHint("Nhập tiêu đề công việc...");
        builder.setView(input);

        // Nút thêm
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String taskTitle = input.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                String dateCreated = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

                // Lưu công việc mới vào cơ sở dữ liệu
                long taskId = databaseHelper.addCongViecCha(taskTitle, dateCreated);
                if (taskId > 0) {
                    Toast.makeText(PhanLoaiCv.this, "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                    loadTasksFromDatabase(); // Cập nhật danh sách công việc
                } else {
                    Toast.makeText(PhanLoaiCv.this, "Có lỗi khi thêm công việc", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PhanLoaiCv.this, "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Hiển thị hộp thoại
        builder.show();
    }
    // Hiển thị hộp thoại xác nhận xóa
    private void showDeleteConfirmationDialog(CongViecCha task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa công việc");
        builder.setMessage("Bạn có chắc chắn muốn xóa công việc này không?");

        // Nút Xóa
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            boolean success = databaseHelper.deleteCongViecCha(task.getId()); // Xóa khỏi cơ sở dữ liệu
            if (success) {
                Toast.makeText(PhanLoaiCv.this, "Xóa công việc thành công", Toast.LENGTH_SHORT).show();
                loadTasksFromDatabase(); // Cập nhật danh sách sau khi xóa
            } else {
                Toast.makeText(PhanLoaiCv.this, "Có lỗi xảy ra khi xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Hiển thị hộp thoại
        builder.show();
    }

    // Tải danh sách công việc từ cơ sở dữ liệu
    private void loadTasksFromDatabase() {
        List<CongViecCha> tasks = databaseHelper.getAllCongViecCha(); // Lấy danh sách từ cơ sở dữ liệu
        adapter.clear();
        adapter.addAll(tasks); // Thêm vào adapter
        adapter.notifyDataSetChanged(); // Cập nhật ListView
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phan_loai, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

