package com.example.appquanlycanhan.profile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PieChartView extends View {
    private Paint paint;
    private float completedTasksPercentage; // Tỷ lệ hoàn thành
    private float incompleteTasksPercentage; // Tỷ lệ chưa hoàn thành
    private int completedTasks; // Số lượng nhiệm vụ đã hoàn thành
    private int incompleteTasks; // Số lượng nhiệm vụ chưa hoàn thành

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);  // Bật anti-aliasing cho các cạnh mượt mà
    }

    public void setData(int completedTasks, int incompleteTasks) {
        int totalTasks = completedTasks + incompleteTasks; // tổng nhiệm vụ
        if (totalTasks > 0) {
            this.completedTasks = completedTasks;
            this.incompleteTasks = incompleteTasks;
            completedTasksPercentage = (float) completedTasks / totalTasks * 360; // Tính tỷ lệ hoàn thành
            incompleteTasksPercentage = (float) incompleteTasks / totalTasks * 360; // Tính tỷ lệ chưa hoàn thành
        } else {
            completedTasksPercentage = 0;
            incompleteTasksPercentage = 0;
        }
        invalidate(); // Yêu cầu vẽ lại
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 - 20; // Bán kính của biểu đồ

        // Vẽ phần hoàn thành
        paint.setColor(Color.parseColor("#F2DDDC")); // Màu hoàn thành
        canvas.drawArc(20, 20, width - 20, height - 20, 0, completedTasksPercentage, true, paint);

        // Vẽ phần chưa hoàn thành
        paint.setColor(Color.parseColor("#E3AADD")); // Màu chưa hoàn thành
        canvas.drawArc(20, 20, width - 20, height - 20, completedTasksPercentage, incompleteTasksPercentage, true, paint);

        // Vẽ số liệu hoàn thành
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        String completedText = completedTasks + " Hoàn thành";
        String incompleteText = incompleteTasks + " Chưa hoàn thành";

        // Vẽ số liệu lên biểu đồ
        canvas.drawText(completedText, width / 4, height / 2 - 20, paint);
        canvas.drawText(incompleteText, 3 * width / 4, height / 2 + 40, paint);
    }
}
