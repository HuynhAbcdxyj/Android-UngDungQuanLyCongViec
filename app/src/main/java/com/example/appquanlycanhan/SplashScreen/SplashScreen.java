package com.example.appquanlycanhan.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appquanlycanhan.MainActivity.MainActivity;
import com.example.appquanlycanhan.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Đặt thời gian chờ cho Splash Screen
        new Handler().postDelayed(() -> {
            // Chuyển đến MainActivity sau 3 giây
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // Kết thúc SplashScreen để không quay lại
        }, 1000); // Thời gian chờ 3000ms (3 giây)
    }
}