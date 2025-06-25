package com.example.appquanlycanhan.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanlycanhan.R;

import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.AwardViewHolder> {
    private List<Award> awards;

    public AwardAdapter(List<Award> awards) {
        this.awards = awards;
    }

    @NonNull
    @Override
    public AwardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_award, parent, false);
        return new AwardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwardViewHolder holder, int position) {
        Award award = awards.get(position);
        holder.awardName.setText(award.getName());
        holder.awardDescription.setText(award.getDescription());
        holder.awardIcon.setImageResource(award.getIconResId());


        // Thay đổi nền của item dựa trên trạng thái mở khóa
        if (award.isUnlocked()) {
            holder.itemView.setBackgroundColor(0xFFF4E7F8); // Màu nền F4E7F8
        } else {
            holder.itemView.setBackgroundColor(R.drawable.rounded_button_background); // Màu nền mặc định (trắng)
        }
    }

    @Override
    public int getItemCount() {
        return awards.size();
    }

    static class AwardViewHolder extends RecyclerView.ViewHolder {
        TextView awardName;
        TextView awardDescription;
        ImageView awardIcon;

        AwardViewHolder(View itemView) {
            super(itemView);
            awardName = itemView.findViewById(R.id.award_name);
            awardDescription = itemView.findViewById(R.id.award_description);
            awardIcon = itemView.findViewById(R.id.award_icon);
        }
    }
}
