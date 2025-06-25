package com.example.appquanlycanhan.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.appquanlycanhan.DatabaseHelper.NhiemVuDocLap;
import com.example.appquanlycanhan.R;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<NhiemVuDocLap> {
    private OnTaskStatusChangedListener listener;
    private List<NhiemVuDocLap> tasks;


    // Giao diện để lắng nghe khi trạng thái nhiệm vụ thay đổi
    public interface OnTaskStatusChangedListener {
        void onTaskStatusChanged(NhiemVuDocLap task, boolean isCompleted);
    }

    public TaskAdapter(Context context, List<NhiemVuDocLap> tasks, OnTaskStatusChangedListener listener) {
        super(context, 0, tasks);
        this.tasks = tasks; // Gán danh sách nhiệm vụ vào biến lớp
        this.listener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NhiemVuDocLap task = getItem(position);

        if (task == null) {
            return convertView; // Không làm gì nếu nhiệm vụ là null
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Ánh xạ các thành phần giao diện
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textTitle = convertView.findViewById(R.id.TieuDe);
        TextView textTime = convertView.findViewById(R.id.ThoiGian);

        // Đặt giá trị cho các thành phần giao diện
        textTitle.setText(task.getTieuDe());
        textTime.setText(task.getThoiGianNhacNho());

        // Loại bỏ sự kiện cũ trước khi đặt giá trị
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(task.isCompleted());

        // Thiết lập sự kiện mới cho checkbox
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onTaskStatusChanged(task, isChecked);
            }
        });

        return convertView;
    }
}
