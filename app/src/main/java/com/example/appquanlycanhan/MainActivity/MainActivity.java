package com.example.appquanlycanhan.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appquanlycanhan.DangNhap.LoginActivity;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import com.example.appquanlycanhan.databinding.ActivityMainBinding;
import com.example.appquanlycanhan.home.HomeFragment;
import com.example.appquanlycanhan.profile.ProfileFragment;
import com.example.appquanlycanhan.setting.SettingFragment;
import com.example.appquanlycanhan.tasks.TaskFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    FloatingActionButton add;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton nutMenu , btnMucKhac;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_menu);
        nutMenu = findViewById(R.id.imbMenu);
        btnMucKhac = findViewById(R.id.btnMucKhac);
        searchView = findViewById(R.id.svTimKiem);

        // xu ly su kien tim kiem
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        nutMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        // chuyển tab home , nhiệm vụ, tài khoản
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.list) {
                replaceFragment(new TaskFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (id == R.id.setting) {
                replaceFragment(new SettingFragment());
            }
            return true;
        });
    }

    private void handleSearch(String query){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof SearchableFragment){
            ((SearchableFragment) currentFragment).onSearch(query);
        }
    }

    // Xử lý thanh công cụ có thể hiển thị được ở mọi form và trừ form profile
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        //kiểm tra nếu fragment là ProfileFragment thì ẩn thanh công cụ bên trên
        LinearLayout topContent = findViewById(R.id.top_content);
        if(fragment instanceof ProfileFragment){
            topContent.setVisibility(View.GONE); // ẩn thanh công cụ
        } else if (fragment instanceof SettingFragment) {
            topContent.setVisibility(View.GONE); // ẩn thanh công cụ
        } else {
            topContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("NAVIGATION", "Menu item clicked: " + item.getTitle());
        int id = item.getItemId();
        if (id == R.id.thay_doi_ghi_chu_nhanh) {
            ThayDoiGhiChuNhanh();
            return true;
        }  else if (id == R.id.doi_mat_khau) {
            showChangePasswordDialog();
            return true;
        } else if (id == R.id.dang_xuat) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Có", (dialog, which) -> dangXuat());
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.create().show();
            return true;
        }
        return false;
    }

    private void dangXuat() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // Đặt trạng thái đăng xuất
        editor.remove("username"); // Xóa thông tin người dùng (nếu cần)
        editor.apply();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Loại bỏ các Activity khác khỏi stack
        startActivity(intent);
        finish(); // Kết thúc MainActivity
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Kiểm tra nếu chạm bên ngoài NavigationView, không đóng drawer ngay lập tức
            View navView = findViewById(R.id.nav_menu);
            if (navView != null && !navView.dispatchTouchEvent(ev)) {
                return false; // Không đóng drawer
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void ThayDoiGhiChuNhanh() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout từ XML
        View dialogView = getLayoutInflater().inflate(R.layout.thay_doi_ghi_chu_nhanh, null);
        EditText input = dialogView.findViewById(R.id.et_input_note);

        builder.setView(dialogView);

        builder.setPositiveButton("Thay đổi", (dialog, which) -> {
            String newNote = input.getText().toString().trim();
            if (!newNote.isEmpty()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentById(R.id.frame_layout);

                if (homeFragment != null) {
                    homeFragment.updateGhiChuNhanh(newNote);
                } else {
                    Toast.makeText(this, "Không tìm thấy HomeFragment", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ghi chú nhanh không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Tạo Dialog nhỏ gọn
        AlertDialog dialog = builder.create();
        dialog.show();

        // Điều chỉnh kích thước dialog
        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), // Chiều ngang 80% màn hình
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_doi_mat_khau, null);
        builder.setView(dialogView);

        EditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);

        builder.setPositiveButton("Đổi mật khẩu", (dialog, which) -> {
            String oldPassword = etOldPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String currentUser = sharedPreferences.getString("username", null);

            if (currentUser == null) {
                Toast.makeText(this, "Không tìm thấy người dùng hiện tại", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

            if (dbHelper.checkUser(currentUser, oldPassword)) {
                boolean updated = dbHelper.updateUserPassword(currentUser, newPassword);
                if (updated) {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
