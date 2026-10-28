package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.model.Contact;

/**
 * Màn hình Chi tiết liên hệ (ContactDetailActivity).
 * Hiển thị đầy đủ thông tin của liên hệ đã chọn:
 * - Gọi điện (Intent ACTION_DIAL)
 * - Đổi trạng thái yêu thích (đồng bộ về MainActivity)
 * - Xóa liên hệ (có hộp thoại xác nhận)
 * - Quay lại màn hình danh sách
 */
public class ContactDetailActivity extends AppCompatActivity {

    private ImageButton btnHeaderBack;
    private FrameLayout layoutAvatar;
    private TextView tvAvatarLetter;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvGroup;
    private TextView tvGender;
    private TextView tvNote;
    private CheckBox cbFavorite;
    private Button btnCall;
    private Button btnBack;
    private Button btnDelete;

    private Contact contact;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        initViews();
        loadDataFromIntent();
        displayContactData();
        setupListeners();
        setupBackPressHandler();
    }

    private void initViews() {
        btnHeaderBack = findViewById(R.id.btnHeaderBack);
        layoutAvatar = findViewById(R.id.layoutAvatar);
        tvAvatarLetter = findViewById(R.id.tvAvatarLetter);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvGroup = findViewById(R.id.tvGroup);
        tvGender = findViewById(R.id.tvGender);
        tvNote = findViewById(R.id.tvNote);
        cbFavorite = findViewById(R.id.cbFavorite);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void loadDataFromIntent() {
        contact = (Contact) getIntent().getSerializableExtra(MainActivity.EXTRA_CONTACT);
        position = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);

        if (contact == null) {
            finish();
        }
    }

    private void displayContactData() {
        if (contact == null) return;

        tvName.setText(contact.getName());
        tvPhone.setText(contact.getPhone());
        tvGroup.setText(contact.getGroup() != null ? contact.getGroup() : "Khác");
        tvGender.setText(contact.getGender() != null ? contact.getGender() : getString(R.string.gender_male));

        if (!TextUtils.isEmpty(contact.getNote())) {
            tvNote.setText(contact.getNote());
        } else {
            tvNote.setText(R.string.detail_note_empty);
        }

        cbFavorite.setChecked(contact.isFavorite());

        // Cập nhật chữ cái đại diện và màu sắc avatar
        tvAvatarLetter.setText(contact.getAvatarLetter());
        int avatarColor = ContextCompat.getColor(this, contact.getAvatarColorRes());
        layoutAvatar.setBackgroundTintList(ColorStateList.valueOf(avatarColor));
    }

    private void setupListeners() {
        // Nút Back trên Header
        btnHeaderBack.setOnClickListener(v -> handleBackWithResult());

        // Thay đổi trạng thái yêu thích
        cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (contact != null) {
                contact.setFavorite(isChecked);
            }
        });

        // 6. Nút Gọi điện: Dùng Intent ACTION_DIAL mở màn hình quay số
        btnCall.setOnClickListener(v -> {
            if (contact != null && !TextUtils.isEmpty(contact.getPhone())) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                startActivity(dialIntent);
            }
        });

        // 7. Nút Quay lại
        btnBack.setOnClickListener(v -> handleBackWithResult());

        // 8. Nút Xóa liên hệ
        btnDelete.setOnClickListener(v -> confirmDeleteContact());
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackWithResult();
            }
        });
    }

    /**
     * Trả về kết quả cho MainActivity và đóng Activity
     */
    private void handleBackWithResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.EXTRA_CONTACT, contact);
        resultIntent.putExtra(MainActivity.EXTRA_POSITION, position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Hộp thoại xác nhận trước khi xóa liên hệ
     */
    private void confirmDeleteContact() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_message))
                .setPositiveButton(R.string.dialog_btn_confirm, (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_POSITION, position);
                    setResult(MainActivity.RESULT_DELETED, resultIntent);
                    finish();
                })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .show();
    }
}

