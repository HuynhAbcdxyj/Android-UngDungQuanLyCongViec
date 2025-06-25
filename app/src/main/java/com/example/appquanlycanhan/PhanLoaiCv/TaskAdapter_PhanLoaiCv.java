package com.example.appquanlycanhan.PhanLoaiCv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.appquanlycanhan.DatabaseHelper.CongViecCha;
import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import java.util.List;

public class TaskAdapter_PhanLoaiCv extends ArrayAdapter<CongViecCha> {
    private Context context;
    private int resource;
    private List<CongViecCha> congViecChaList;


    public TaskAdapter_PhanLoaiCv(Context context, int resource, List<CongViecCha> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.congViecChaList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tvTaskTitle = convertView.findViewById(R.id.tvTaskTitle);
        TextView tvTaskDate = convertView.findViewById(R.id.tvTaskDate);
        ProgressBar progressBar = convertView.findViewById(R.id.taskProgressBar);
        TextView tvProgressPercentage = convertView.findViewById(R.id.tvProgressPercentage); // Thêm TextView hiển thị %

        CongViecCha task = congViecChaList.get(position);

        // Hiển thị thông tin công việc cha
        tvTaskTitle.setText(task.getTieuDe());
        tvTaskDate.setText(task.getNgayTao());

        // Lấy số liệu từ database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        int completedTasks = dbHelper.getCompletedTasksCount(task.getId()); // Số công việc con đã hoàn thành theo công việc cha
        int totalTasks = dbHelper.getTotalTasksCount(task.getId()); // Tổng số công việc con theo công việc cha

        // Tính phần trăm hoàn thành
        int progress = totalTasks > 0 ? (completedTasks * 100 / totalTasks) : 0;

        //Điều kiện:
        //Nếu totalTasks > 0: Áp dụng công thức tính.
        //Nếu totalTasks == 0: Đặt giá trị mặc định progress = 0 để tránh chia cho 0.
        //Ví dụ:
        //
        //Nếu completedTasks = 3 và totalTasks = 5, thì progress = (3 * 100 / 5) = 60%.
        //Nếu không có công việc nào (totalTasks = 0), thì progress = 0.

        // Cập nhật ProgressBar và TextView
        progressBar.setProgress(progress);
        tvProgressPercentage.setText(progress + "%");

        return convertView;
    }


}

