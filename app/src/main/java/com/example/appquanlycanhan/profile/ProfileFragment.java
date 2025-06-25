package com.example.appquanlycanhan.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanlycanhan.R;
import com.example.appquanlycanhan.tasks.TaskDataProvider;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private Spinner spinnerProfession;
    private TextView profileNameTextView;
    private Button btnThayDoiTen;
    private SharedPreferences sharedPreferences;
    private TextView textNhiemVuHoanTat;
    private TextView textNhiemVuChuaXong;
    private PieChartView pieChartView;
    private RecyclerView recyclerViewAwards;

    private AwardAdapter awardsAdapter;

    private List<Award> awardList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        pieChartView = rootView.findViewById(R.id.pieChartView);
        pieChartView.setData(4, 6);
        recyclerViewAwards = rootView.findViewById(R.id.recyclerViewAwards);
        recyclerViewAwards.setLayoutManager(new LinearLayoutManager(getContext()));
        awardList = new ArrayList<>();
        // Thêm danh hiệu mẫu
        awardList.add(new Award("Người mới", "Hoàn thành 5 nhiệm vụ đầu tiên", R.drawable.tap_su));
        awardList.add(new Award("Siêu hạng", "Hoàn thành 50 nhiệm vụ", R.drawable.sieu_hang));
        awardList.add(new Award("Chiến thuật gia", "Tối ưu hóa thời gian 15 nhiệm vụ", R.drawable.chien_thuat_gia));
        awardList.add(new Award("Chuỗi hoàn thành nhiệm vụ", "Hoàn thành 100 nhiệm vụ", R.drawable.chuoi_hoan_thanh_nhiem_vu));
        awardList.add(new Award("Kẻ chinh phục thời gian", "Hoàn thành 10 nhiệm vụ trong 30 phút", R.drawable.ke_chinh_phuc_thoi_gian));
        awardList.add(new Award("Người kỷ luật", "Hoàn thành 20 nhiệm vụ đúng hạn", R.drawable.nguoi_ky_luat));
        awardList.add(new Award("Người tối ưu hóa", "Hoàn thành 10 nhiệm vụ trong 15 phút", R.drawable.nguoi_toi_uu_hoa));
        awardList.add(new Award("Chuyên gia", "Hoàn thành 100 nhiệm vụ", R.drawable.chuyen_gia));
        awardList.add(new Award("Người tổ chức", "Hoàn thành 200 nhiệm vụ", R.drawable.nguoi_to_chuc));
        // Thêm nhiều danh hiệu khác ở đây
        awardsAdapter = new AwardAdapter(awardList);
        recyclerViewAwards.setAdapter(awardsAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tham chiếu Spinner và các phần tử khác từ layout
        spinnerProfession = view.findViewById(R.id.spinner_profession);
        profileNameTextView = view.findViewById(R.id.profile_name);
        btnThayDoiTen = view.findViewById(R.id.btn_thay_doi_ten);
        textNhiemVuHoanTat = view.findViewById(R.id.textNhiemVuHoanTat);
        textNhiemVuChuaXong = view.findViewById(R.id.textNhiemVuChuaXong);
        updateTaskCounts();

        // Lấy SharedPreferences để lưu tên người dùng
        sharedPreferences = requireContext().getSharedPreferences("User Prefs", requireContext().MODE_PRIVATE);

        // Lấy tên đã lưu trước đó từ SharedPreferences
        String savedName = sharedPreferences.getString("userName", "OLIVIA WILSON");
        profileNameTextView.setText(savedName);

        // Khôi phục nghề nghiệp đã lưu từ SharedPreferences
        String savedProfession = sharedPreferences.getString("userProfession", "Chọn nghề nghiệp");
        setProfessionInSpinner(savedProfession);

        // Lắng nghe sự kiện khi nhấn nút thay đổi tên
        btnThayDoiTen.setOnClickListener(v -> showChangeNameDialog());

        spinnerProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProfession = parent.getItemAtPosition(position).toString();
                // Lưu nghề nghiệp vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userProfession", selectedProfession);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì cả
            }
        });
    }
    private void setProfessionInSpinner(String profession) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.professions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfession.setAdapter(adapter);
        // Tìm vị trí của nghề nghiệp đã lưu và thiết lập cho Spinner
        int spinnerPosition = adapter.getPosition(profession);
        spinnerProfession.setSelection(spinnerPosition);
    }
    private void showChangeNameDialog() {
        // Tạo EditText để người dùng nhập tên mới
        final EditText input = new EditText(getContext());
        input.setHint("Nhập tên mới");
        input.setText(profileNameTextView.getText()); // Đặt tên hiện tại làm gợi ý
        // Tạo hộp thoại thay đổi tên
        new AlertDialog.Builder(requireContext())
                .setTitle("Thay đổi tên")
                .setMessage("Nhập tên mới của bạn")
                .setView(input)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString().trim();
                        if (!newName.isEmpty()) {
                            // Cập nhật tên mới trong TextView
                            profileNameTextView.setText(newName);

                            // Lưu tên mới vào SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userName", newName);
                            editor.apply();
                            Toast.makeText(requireContext(), "Tên đã được thay đổi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void updateTaskCounts() {
        TaskDataProvider taskDataProvider = new TaskDataProvider(requireContext());

        int completedTasks = taskDataProvider.getCompletedTasksCount();
        int incompleteTasks = taskDataProvider.getIncompleteTasksCount();
        checkAwards(completedTasks);
        // Cập nhật dữ liệu cho biểu đồ
        pieChartView.setData(completedTasks, incompleteTasks);
    }

    private void checkAwards(int completedTasksCount) {

        for (Award award : awardList) {
            // Kiểm tra điều kiện để mở khóa danh hiệu
            if (completedTasksCount >= 5 && award.getName().equals("Người mới")) {
                award.setUnlocked(true);
            } else if (completedTasksCount >= 50 && award.getName().equals("Siêu hạng")) {
                award.setUnlocked(true);
            } else if (completedTasksCount >= 100 && award.getName().equals("Chuỗi hoàn thành nhiệm vụ")) {
                award.setUnlocked(true);
            }
            // Thêm các điều kiện khác cho các danh hiệu khác nếu cần
        }
        awardsAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị thay đổi
    }
}
