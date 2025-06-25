package com.example.appquanlycanhan.setting;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.appquanlycanhan.DangNhap.LoginActivity;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingFragment extends Fragment {

    private Switch darkModeSwitch, notificationSwitch;
    private SharedPreferences preferences;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Khởi tạo các View
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        notificationSwitch = view.findViewById(R.id.switch_notifications);

        // Khởi tạo DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        // SharedPreferences
        preferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Kiểm tra trạng thái Dark Mode ban đầu
        boolean isDarkMode = preferences.getBoolean("darkMode", false);
        darkModeSwitch.setChecked(isDarkMode);  // Đảm bảo trạng thái ban đầu là sáng (false)

        // Áp dụng chế độ sáng/tối khi khởi động
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Xử lý sự kiện chuyển đổi Dark Mode
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Chỉ thay đổi chế độ sáng/tối khi người dùng thay đổi trạng thái
            if (isChecked) {
                // Chuyển sang chế độ tối
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                savePreference("darkMode", true);  // Lưu trạng thái Dark Mode là bật
                Toast.makeText(getContext(), "Bật chế độ tối", Toast.LENGTH_SHORT).show();
            } else {
                // Chuyển sang chế độ sáng
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                savePreference("darkMode", false);  // Lưu trạng thái Dark Mode là tắt
                Toast.makeText(getContext(), "Bật chế độ sáng", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện bật/tắt thông báo
        notificationSwitch.setChecked(preferences.getBoolean("notifications", true));
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference("notifications", isChecked);
            if (isChecked) {
                enableNotifications();
            } else {
                disableNotifications();
            }
        });

        // Chức năng chọn ngôn ngữ
        view.findViewById(R.id.languageOption).setOnClickListener(v -> showLanguageDialog());

        // Chức năng thông tin ứng dụng
        view.findViewById(R.id.aboutAppOption).setOnClickListener(v -> startActivity(new Intent(getContext(), AboutAppActivity.class)));

        // Chức năng điều khoản sử dụng
        view.findViewById(R.id.termsOption).setOnClickListener(v -> startActivity(new Intent(getContext(), TermsConditionsActivity.class)));

        // Chức năng chính sách bảo mật
        view.findViewById(R.id.privacyPolicyOption).setOnClickListener(v -> startActivity(new Intent(getContext(), PrivacyPolicyActivity.class)));

        // Chức năng chia sẻ ứng dụng
        view.findViewById(R.id.shareAppOption).setOnClickListener(v -> shareApp());

        // Chức năng xóa dữ liệu
        view.findViewById(R.id.clearDataOption).setOnClickListener(v -> showClearDataConfirmation());

        return view;
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();

        if (key.equals("darkMode")) {
            // Chuyển chế độ sáng/tối mà không khởi động lại Activity
            if (value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(getContext(), "Bật chế độ tối", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(getContext(), "Bật chế độ sáng", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Bật thông báo
    private void enableNotifications() {
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            Toast.makeText(getContext(), "Thông báo đã được bật", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Không thể bật thông báo", Toast.LENGTH_SHORT).show();
        }
    }

    // Tắt thông báo
    private void disableNotifications() {
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
            Toast.makeText(getContext(), "Thông báo đã được tắt", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Không thể tắt thông báo", Toast.LENGTH_SHORT).show();
        }
    }

    // Hộp thoại chọn ngôn ngữ
    private void showLanguageDialog() {
        String[] languages = {"Tiếng Anh", "Tiếng Việt"};
        new AlertDialog.Builder(getContext())
                .setTitle("Chọn ngôn ngữ")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = which == 0 ? "en" : "vi";
                    changeLanguage(selectedLanguage);
                })
                .show();
    }
    private void changeLanguage(String languageCode) {
        // Tạo đối tượng Locale mới cho ngôn ngữ được chọn
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Cập nhật cấu hình của ứng dụng với ngôn ngữ mới
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Lưu ngôn ngữ vào SharedPreferences
        saveLanguagePreference(languageCode);

        // Gọi lại Activity để áp dụng ngôn ngữ mới
        requireActivity().recreate();
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    private String getLanguagePreference() {
        return preferences.getString("language", "vi");  // mặc định là Tiếng Việt
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "Hãy tải ngay ứng dụng này tại: https://www.amazon.com/gp/product/B0DPLBNCHL";
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Chia sẻ ứng dụng qua"));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void showClearDataConfirmation() {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa dữ liệu")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ dữ liệu không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> clearAllData())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private boolean clearSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        return editor.clear().commit();
    }

    private boolean clearDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());  // Khởi tạo DatabaseHelper
        return databaseHelper.deleteAllData();  // Gọi phương thức deleteAllData từ đối tượng
    }


    private void clearFirebaseData() {
        if (isNetworkAvailable()) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            databaseRef.child("user_data").removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firebase", "Dữ liệu Firebase đã được xóa thành công.");
                } else {
                    Log.e("Firebase", "Xóa dữ liệu Firebase thất bại: " + task.getException().getMessage());
                }
            });
        } else {
            Toast.makeText(getContext(), "Không có kết nối mạng, không thể xóa dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAllData() {
        if (clearSharedPreferences()) {
            if (clearDatabase()) {
                clearFirebaseData();

                // Xóa tài khoản người dùng từ cơ sở dữ liệu (dùng tên người dùng đã đăng nhập)
                String username = preferences.getString("username", null);  // Lấy tên người dùng từ SharedPreferences
                if (username != null) {
                    boolean isDeleted = databaseHelper.deleteUser(username);  // Gọi phương thức deleteUser để xóa tài khoản
                    if (isDeleted) {
                        Toast.makeText(getContext(), "Tài khoản đã được xóa!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không thể xóa tài khoản người dùng!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy tài khoản người dùng!", Toast.LENGTH_SHORT).show();
                }

                // Đưa người dùng về màn hình đăng nhập
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Đảm bảo mở lại Activity đăng nhập và xóa tất cả các Activity trước đó
                startActivity(loginIntent);
                requireActivity().finish();  // Đóng Activity hiện tại
            } else {
                Toast.makeText(getContext(), "Không thể xóa dữ liệu từ cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Không thể xóa dữ liệu SharedPreferences!", Toast.LENGTH_SHORT).show();
        }
    }
}

