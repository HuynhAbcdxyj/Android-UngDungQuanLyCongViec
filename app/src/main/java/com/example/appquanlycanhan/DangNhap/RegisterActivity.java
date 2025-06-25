package com.example.appquanlycanhan.DangNhap;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các view
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hiển thị nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24); // Đặt icon mũi tên quay lại
            getSupportActionBar().setTitle(""); // Bỏ tiêu đề mặc định
        }

        // Khởi tạo database helper
        db = new DatabaseHelper(this);

        // Xử lý sự kiện nút đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ho_ten = etUsername.getText().toString().trim();
                String mat_khau = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Kiểm tra các trường dữ liệu
                if (ho_ten.isEmpty() || mat_khau.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!mat_khau.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                } else {
                    // Thêm người dùng vào cơ sở dữ liệu
                    boolean isRegistered = db.addUser(ho_ten, mat_khau);
                    if (isRegistered) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại trang trước
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // Xử lý nút quay lại
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Đóng activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
