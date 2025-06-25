package com.example.appquanlycanhan.tasks;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.DatabaseHelper.NhiemVuDocLap;
import com.example.appquanlycanhan.MainActivity.SearchableFragment;
import com.example.appquanlycanhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;

public class TaskFragment extends Fragment implements SearchableFragment {
    private List<NhiemVuDocLap> incompleteTasks;
    private List<NhiemVuDocLap> completedTasks;
    private TaskAdapter incompleteAdapter;
    private TaskAdapter completedAdapter;
    private FloatingActionButton btnAddTask;
    private List<NhiemVuDocLap> allTasks;

    // Tham chiếu đến các TextView để hiển thị số lượng nhiệm vụ
    private TextView textCompletedTasksCount;
    private TextView textIncompleteTasksCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        // Lấy dữ liệu từ cơ sở dữ liệu
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        allTasks = dbHelper.getAllNhiemVuDocLap();

        // Tham chiếu đến các TextView
        textCompletedTasksCount = rootView.findViewById(R.id.textCompletedTasksCount);
        textIncompleteTasksCount = rootView.findViewById(R.id.textIncompleteTasksCount);


        btnAddTask = rootView.findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(v -> {
            AddTaskBottomSheet bottomSheet = new AddTaskBottomSheet();
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });

        // Phân loại nhiệm vụ
        incompleteTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        for (NhiemVuDocLap task : allTasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            } else {
                incompleteTasks.add(task);
            }
        }

        // Adapter cho danh sách nhiệm vụ chưa hoàn tất
        incompleteAdapter = new TaskAdapter(requireContext(), incompleteTasks, (task, isCompleted) -> {
            // Khi nhiệm vụ được đánh dấu là hoàn tất
            if (isCompleted) {
                moveTaskToCompleted(task, dbHelper);
            }
        });
        ListView incompleteListView = rootView.findViewById(R.id.taskListViewChuaXong);
        incompleteListView.setAdapter(incompleteAdapter);

        // Bắt sự kiện khi nhấn vào nhiệm vụ chưa hoàn tất
        incompleteListView.setOnItemClickListener((parent, view, position, id) -> {
            NhiemVuDocLap task = incompleteTasks.get(position);
            openBottomSheetForTask(task);
        });

        // Xử lý sự kiện xóa cho nhiệm vụ chưa hoàn tất
        incompleteListView.setOnItemLongClickListener((parent, view, position, id) -> {
            NhiemVuDocLap task = incompleteTasks.get(position);
            showDeleteDialog(task, position, dbHelper, incompleteTasks, incompleteAdapter);
            return true;
        });

        // Adapter cho danh sách nhiệm vụ đã hoàn tất
        completedAdapter = new TaskAdapter(requireContext(), completedTasks, (task, isCompleted) -> {
            // Khi nhiệm vụ được đánh dấu là chưa hoàn tất
            if (!isCompleted) {
                moveTaskToIncomplete(task, dbHelper);
            }
        });

        ListView completedListView = rootView.findViewById(R.id.taskListViewHoanTat);
        completedListView.setAdapter(completedAdapter);

        // Bắt sự kiện khi nhấn vào nhiệm vụ đã hoàn tất
        completedListView.setOnItemClickListener((parent, view, position, id) -> {
            NhiemVuDocLap task = completedTasks.get(position);
            openBottomSheetForTask(task);
        });

        // Xử lý sự kiện xóa cho nhiệm vụ đã hoàn tất
        completedListView.setOnItemLongClickListener((parent, view, position, id) -> {
            NhiemVuDocLap task = completedTasks.get(position);
            showDeleteDialog(task, position, dbHelper, completedTasks, completedAdapter);
            return true;
        });
        updateTaskCounts();
        return rootView;
    }

    // Hàm hiển thị hộp thoại xác nhận xóa
    private void showDeleteDialog(NhiemVuDocLap task, int position, DatabaseHelper dbHelper,
                                  List<NhiemVuDocLap> taskList, TaskAdapter adapter) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Bạn có chắc chắn muốn xóa nhiệm vụ này?")
                .setCancelable(false)
                .setPositiveButton("Xóa", (dialog, id) -> {
                    // Xóa nhiệm vụ khỏi cơ sở dữ liệu
                    if (dbHelper.deleteTaskById(task.getId())) {
                        taskList.remove(position); // Xóa nhiệm vụ khỏi danh sách
                        adapter.notifyDataSetChanged(); // Cập nhật giao diện
                        Toast.makeText(requireContext(), "Nhiệm vụ đã bị xóa", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Lỗi khi xóa nhiệm vụ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss()) // Hủy thao tác
                .create()
                .show();
    }

    // Hàm chuyển nhiệm vụ từ chưa hoàn thành sang đã hoàn thành
    private void moveTaskToCompleted(NhiemVuDocLap task, DatabaseHelper dbHelper) {
        incompleteTasks.remove(task); // Xóa nhiệm vụ khỏi danh sách chưa hoàn thành
        completedTasks.add(0, task); // Thêm nhiệm vụ vào đầu danh sách đã hoàn thành
        task.setCompleted(true); // Cập nhật trạng thái nhiệm vụ
        dbHelper.updateTaskStatus(task); // Cập nhật trạng thái trong cơ sở dữ liệu
        incompleteAdapter.notifyDataSetChanged();
        completedAdapter.notifyDataSetChanged();
    }

    // Hàm cập nhật số lượng nhiệm vụ

    private void updateTaskCounts() {
        textCompletedTasksCount.setText(String.valueOf(completedTasks.size()));
        textIncompleteTasksCount.setText(String.valueOf(incompleteTasks.size()));
    }
    // Hàm chuyển nhiệm vụ từ đã hoàn thành sang chưa hoàn thành
    private void moveTaskToIncomplete(NhiemVuDocLap task, DatabaseHelper dbHelper) {
        completedTasks.remove(task); // Xóa nhiệm vụ khỏi danh sách đã hoàn thành
        incompleteTasks.add(0, task); // Thêm nhiệm vụ vào đầu danh sách chưa hoàn thành
        task.setCompleted(false); // Cập nhật trạng thái nhiệm vụ
        dbHelper.updateTaskStatus(task); // Cập nhật trạng thái trong cơ sở dữ liệu
        completedAdapter.notifyDataSetChanged();
        incompleteAdapter.notifyDataSetChanged();
    }

    // Hàm mở BottomSheet và truyền dữ liệu
    private void openBottomSheetForTask(NhiemVuDocLap task) {
        AddTaskBottomSheet bottomSheet = new AddTaskBottomSheet();
        Bundle args = new Bundle();
        args.putInt("id", task.getId());
        args.putString("tieu_de", task.getTieuDe());
        args.putString("thoi_gian_nhac_nho", task.getThoiGianNhacNho());
        bottomSheet.setArguments(args);
        bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
    }

    // Hàm tìm kiếm
    @Override
    public void onSearch(String query) {
        List<NhiemVuDocLap> filteredTasks = new ArrayList<>();
        if (query != null && !query.isEmpty()) {
            for (NhiemVuDocLap task : allTasks) {
                if (task.getTieuDe().toLowerCase().contains(query.toLowerCase())) {
                    filteredTasks.add(task);
                }
            }
        } else {
            filteredTasks = allTasks;
        }

        // Cập nhật lại danh sách nhiệm vụ
        categorizeTasks(filteredTasks);
    }

    // Phân loại nhiệm vụ theo trạng thái
    private void categorizeTasks(List<NhiemVuDocLap> tasks) {
        incompleteTasks.clear();
        completedTasks.clear();
        for (NhiemVuDocLap task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            } else {
                incompleteTasks.add(task);
            }
        }
        incompleteAdapter.notifyDataSetChanged();
        completedAdapter.notifyDataSetChanged();
    }

    public int getCompletedTasksCount() {
        return completedTasks.size();
    }

    public int getIncompleteTasksCount() {
        return incompleteTasks.size();
    }
}
