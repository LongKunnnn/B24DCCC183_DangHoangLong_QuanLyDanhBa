package com.example.quanlydanhba.model;

import com.example.quanlydanhba.R;

import java.io.Serializable;
import java.util.Objects;

/**
 * Lớp Contact đại diện cho một liên hệ trong danh bạ.
 * Triển khai Serializable để truyền đối tượng qua lại giữa các Activity bằng Intent.
 */
public class Contact implements Serializable {
    private String name;
    private String phone;
    private String group;
    private String gender;
    private boolean isFavorite;
    private String note;

    public Contact() {
    }

    public Contact(String name, String phone, String group, String gender, boolean isFavorite, String note) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.isFavorite = isFavorite;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Lấy chữ cái đại diện từ tên liên hệ (lấy chữ cái đầu của từ cuối cùng - tên chính).
     * Ví dụ: "Nguyễn Văn An" -> "A", "Trần Thị Bình" -> "B", "Lê Minh Châu" -> "C"
     */
    public String getAvatarLetter() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length > 0) {
            String lastWord = parts[parts.length - 1];
            if (!lastWord.isEmpty()) {
                return String.valueOf(lastWord.charAt(0)).toUpperCase();
            }
        }
        return String.valueOf(trimmed.charAt(0)).toUpperCase();
    }

    /**
     * Lấy màu nền đại diện cho avatar theo tên để tạo giao diện trực quan, đẹp mắt.
     */
    public int getAvatarColorRes() {
        String letter = getAvatarLetter();
        if (letter.isEmpty()) {
            return R.color.avatar_teal;
        }
        char c = letter.charAt(0);
        switch (c % 6) {
            case 0:
                return R.color.avatar_teal;
            case 1:
                return R.color.avatar_deep_orange;
            case 2:
                return R.color.avatar_purple;
            case 3:
                return R.color.avatar_blue;
            case 4:
                return R.color.avatar_pink;
            default:
                return R.color.avatar_green;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(phone, contact.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}

