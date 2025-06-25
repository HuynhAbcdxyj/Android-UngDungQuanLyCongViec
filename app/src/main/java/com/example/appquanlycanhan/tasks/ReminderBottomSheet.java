package com.example.appquanlycanhan.tasks;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appquanlycanhan.DatabaseHelper.DatabaseHelper;
import com.example.appquanlycanhan.R;
import com.example.appquanlycanhan.home.addHome;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class ReminderBottomSheet extends BottomSheetDialogFragment {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private OnReminderSetListener listener;

    public interface OnReminderSetListener {
        void onReminderSet(int year, int month, int day, int hour, int minute);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reminder_picker_bottom_sheet_dialog, container, false);

        // Ánh xạ view
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        Button cancelButton = view.findViewById(R.id.btnHuy);
        Button okButton = view.findViewById(R.id.btnOk);

        // Nút Hủy
        cancelButton.setOnClickListener(v -> dismiss());

        // Nút OK
        okButton.setOnClickListener(v -> {
            // Lấy giá trị từ DatePicker và TimePicker
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            // Định dạng dữ liệu
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year);
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            if (getActivity() instanceof addHome) {
                addHome activity = (addHome) getActivity();
                activity.onReminderTimeSelected(selectedTime, selectedDate);
            }

            // Truyền dữ liệu về TargetFragment
            if (getTargetFragment() instanceof AddTaskBottomSheet) {
                ((AddTaskBottomSheet) getTargetFragment()).onDateTimeSelected(selectedDate, selectedTime);
            }

            dismiss();
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // Thiết lập hiển thị 24 giờ
        // Đặt trạng thái mở rộng tối đa cho BottomSheet
        View parent = (View) view.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // Thiết lập chiều cao tối đa (nếu cần)
        behavior.setSkipCollapsed(true); // Bỏ trạng thái collapse
    }

}
