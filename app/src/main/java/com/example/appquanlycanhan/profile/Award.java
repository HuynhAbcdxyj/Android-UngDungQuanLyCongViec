package com.example.appquanlycanhan.profile;

public class Award {
    private String name;
    private String description;
    private int iconResId;
    private boolean isUnlocked;

    public Award(String name, String description, int iconResId) {
        this.name = name;
        this.description = description;
        this.iconResId = iconResId;
        this.isUnlocked = false; // Mặc định là chưa mở khóa
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}