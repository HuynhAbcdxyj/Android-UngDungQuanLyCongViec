package com.example.appquanlycanhan.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.appquanlycanhan.DatabaseHelper.CongViecCon;
import com.example.appquanlycanhan.R;

import java.util.List;

public class TaskAddapter_CongViecCon extends ArrayAdapter<CongViecCon> {
    private Context context;
    private List<CongViecCon> congViecConList;
    public TaskAddapter_CongViecCon(Context context, List<CongViecCon> congViecConList){
        super(context, 0, congViecConList);
        this.context = context;
        this.congViecConList = congViecConList;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        }

        CongViecCon congViecCon = congViecConList.get(position);

        // Ánh xạ các view trong task_item.xml
        TextView tvTaskTitle = convertView.findViewById(R.id.tvTaskTitle);
        TextView tvTaskDate = convertView.findViewById(R.id.tvTaskDate);
        TextView tvthoigiannhacnho = convertView.findViewById(R.id.tvThoiGianNhacNho);

        // Thiết lập tiêu đề công việc và ngày giờ cho TextView
        tvTaskTitle.setText(congViecCon.getTieuDe());
        tvTaskDate.setText(congViecCon.getNgayTao()); // Bạn có thể sử dụng ngày nhắc nhở hoặc ngày tạo, tùy theo yêu cầu
        tvthoigiannhacnho.setText("Nhắc nhở vào: "+congViecCon.getThoiGianNhacNho());

        return convertView;
    }

}
