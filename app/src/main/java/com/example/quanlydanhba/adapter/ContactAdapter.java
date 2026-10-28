package com.example.quanlydanhba.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.model.Contact;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách các liên hệ.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnContactClickListener {
        void onContactClick(Contact contact, int position);
    }

    private final Context context;
    private final List<Contact> contactList;
    private final OnContactClickListener listener;

    public ContactAdapter(Context context, List<Contact> contactList, OnContactClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());
        holder.tvAvatarLetter.setText(contact.getAvatarLetter());

        // Đổi màu nền avatar theo chữ cái
        int avatarColor = ContextCompat.getColor(context, contact.getAvatarColorRes());
        holder.layoutAvatar.setBackgroundTintList(ColorStateList.valueOf(avatarColor));

        // Nhóm và kiểu dáng huy hiệu (Badge)
        String group = contact.getGroup() != null ? contact.getGroup() : "Khác";
        holder.tvGroupBadge.setText(group);
        switch (group) {
            case "Gia đình":
                holder.tvGroupBadge.setBackgroundResource(R.drawable.bg_badge_family);
                holder.tvGroupBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_family_text));
                break;
            case "Bạn bè":
                holder.tvGroupBadge.setBackgroundResource(R.drawable.bg_badge_friend);
                holder.tvGroupBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_friend_text));
                break;
            case "Công việc":
                holder.tvGroupBadge.setBackgroundResource(R.drawable.bg_badge_work);
                holder.tvGroupBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_work_text));
                break;
            default:
                holder.tvGroupBadge.setBackgroundResource(R.drawable.bg_badge_other);
                holder.tvGroupBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_other_text));
                break;
        }

        // Dấu sao nếu là liên hệ yêu thích
        holder.ivStar.setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);

        // Sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(contact, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        FrameLayout layoutAvatar;
        TextView tvAvatarLetter;
        TextView tvName;
        TextView tvPhone;
        TextView tvGroupBadge;
        ImageView ivStar;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutAvatar = itemView.findViewById(R.id.layoutAvatar);
            tvAvatarLetter = itemView.findViewById(R.id.tvAvatarLetter);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvGroupBadge = itemView.findViewById(R.id.tvGroupBadge);
            ivStar = itemView.findViewById(R.id.ivStar);
        }
    }
}

