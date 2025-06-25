package com.example.appquanlycanhan.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquanlycanhan.DatabaseHelper.CongViecCha;
import com.example.appquanlycanhan.DatabaseHelper.CongViecCon;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.MainActivity.SearchableFragment;
import com.example.appquanlycanhan.PhanLoaiCv.PhanLoaiCv;
import com.example.appquanlycanhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SearchableFragment {

    private ListView taskListView;
    private List<CongViecCon> congViecConList;
    private DatabaseHelper databaseHelper;
    private LinearLayout menuContainer;  // Container cho menu ngang
    ImageButton btnMucKhac;
    private FloatingActionButton btnAddHome;
    private int currentParentId = -1; // ID của công việc cha hiện tại (-1 là mặc định khi chưa chọn)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        taskListView = rootView.findViewById(R.id.taskListView);
        btnAddHome = rootView.findViewById(R.id.btnAddHome);
        databaseHelper = new DatabaseHelper(getContext());
        menuContainer = rootView.findViewById(R.id.menuContainer);  // LinearLayout chứa các nút công việc cha

        // Lấy danh sách công việc cha từ cơ sở dữ liệu
        List<CongViecCha> congViecChaList = databaseHelper.getAllCongViecCha();

        // Tạo nút Button cho mỗi công việc cha và thêm vào menuContainer
        for (CongViecCha congViecCha : congViecChaList) {
            Button btnCongViec = new Button(getContext());
            btnCongViec.setText(congViecCha.getTieuDe()); // Hiển thị tiêu đề công việc cha

            // Tạo LayoutParams với chiều cao nhỏ hơn và thêm khoảng cách
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Chiều rộng tự động
                    100 // Chiều cao nhỏ hơn (thay đổi giá trị này theo nhu cầu)
            );
            params.setMargins(8, 8, 8, 8); // Thêm khoảng cách bên trái, trên và dưới

            btnCongViec.setLayoutParams(params);
            btnCongViec.setBackgroundColor(Color.parseColor("#E3AADD")); // Đặt màu nền ban đầu
            btnCongViec.setTextColor(Color.BLACK); // Đặt màu chữ
            btnCongViec.setPadding(8, 8, 8, 8); // Thêm padding để nội dung không bị chèn sát viền

            btnCongViec.setOnClickListener(v -> {
                // Đặt lại màu cho tất cả các nút công việc cha về màu ban đầu
                for (int i = 0; i < menuContainer.getChildCount(); i++) {
                    Button button = (Button) menuContainer.getChildAt(i);
                    button.setBackgroundColor(Color.parseColor("#E3AADD")); // Màu nền ban đầu
                }

                // Đặt màu cho nút được nhấn
                btnCongViec.setBackgroundColor(Color.parseColor("#F6BCBA")); // Màu nền khi được chọn

                int idCha = congViecCha.getId();// lưu giữ id của công việc cha hiện tại được chọn
                currentParentId = idCha;
                layDuLieuViecConTheoCha(idCha);
            });

            // Thêm nút vào menuContainer
            menuContainer.addView(btnCongViec);
        }

        // Xử lý sự kiện xóa khi người dùng nhấn giữ vào công việc con
        taskListView.setOnItemLongClickListener((parent, view, position, id) -> {
            CongViecCon selectedTask = congViecConList.get(position);
            new AlertDialog.Builder(getContext())
                    .setMessage("Bạn có chắc chắn muốn xóa công việc này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        databaseHelper.deleteTask(selectedTask.getId());
                        Toast.makeText(getContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                        layDuLieuViecCon(); // Cập nhật danh sách sau khi xóa
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });

        // Xử lý sự kiện bấm vào (single click)
        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            CongViecCon selectedTask = congViecConList.get(position); // Lấy công việc con được chọn
            Intent intent = new Intent(getContext(), addHome.class);
            intent.putExtra("tieuDe", selectedTask.getTieuDe());
            intent.putExtra("ngayTao", selectedTask.getNgayTao());
            startActivity(intent); // Chuyển sang AddHomeActivity
        });

        btnMucKhac = rootView.findViewById(R.id.btnMucKhac);
        // Xử lý sự kiện click để chuyển sang PhanLoaiCv
        btnMucKhac.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PhanLoaiCv.class);
            startActivity(intent); // Bắt đầu Activity mới
        });

        btnAddHome.setOnClickListener(v -> {
            if (currentParentId == -1) {
                Intent intent = new Intent(getActivity(), addHome.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), addHome.class);
                intent.putExtra("parentId", currentParentId);
                startActivity(intent);
            }
        });

        layDuLieuViecCon(); // Lấy dữ liệu công việc con khi khởi động
        return rootView;
    }

    public void layDuLieuViecCon() {
        // Lấy danh sách công việc con từ cơ sở dữ liệu
        congViecConList = databaseHelper.getAllTasks();  // Lấy tất cả công việc con
        TaskAddapter_CongViecCon taskAdapter = new TaskAddapter_CongViecCon(getContext(), congViecConList);
        taskListView.setAdapter(taskAdapter);
    }

    public void layDuLieuViecConTheoCha(int idCha) {
        // Lấy danh sách các công việc con từ cơ sở dữ liệu dựa trên id của công việc cha
        congViecConList = databaseHelper.getTasksByParentId(idCha); // Phương thức getTasksByParentId cần được định nghĩa trong DatabaseHelper
        TaskAddapter_CongViecCon taskAdapter = new TaskAddapter_CongViecCon(getContext(), congViecConList);
        taskListView.setAdapter(taskAdapter);
    }

    @Override
    public void onSearch(String query) {
        if (query != null && !query.isEmpty()) {
            // Lọc danh sách công việc dựa trên tiêu đề
            List<CongViecCon> filteredList = new ArrayList<>();
            for (CongViecCon task : congViecConList) {
                if (task.getTieuDe().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(task);
                }
            }
            // Cập nhật ListView với danh sách đã lọc
            TaskAddapter_CongViecCon taskAdapter = new TaskAddapter_CongViecCon(getContext(), filteredList);
            taskListView.setAdapter(taskAdapter);
        } else {
            // Hiển thị danh sách ban đầu nếu từ khóa rỗng
            layDuLieuViecCon();
        }
    }

    public void updateGhiChuNhanh(String newNote) {
        TextView ghiChuNhanhTextView = getView().findViewById(R.id.ghi_chu_nhanh);
        if (ghiChuNhanhTextView != null) {
            ghiChuNhanhTextView.setText(newNote);
        }
    }
}