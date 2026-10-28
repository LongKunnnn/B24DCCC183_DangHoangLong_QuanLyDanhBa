package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.adapter.ContactAdapter;
import com.example.quanlydanhba.model.Contact;

import java.util.ArrayList;

/**
 * Màn hình Danh sách liên hệ (MainActivity).
 * Quản lý danh sách liên hệ bằng ArrayList<Contact>, hiển thị thống kê tổng số và số yêu thích.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_EXISTING_PHONES = "EXTRA_EXISTING_PHONES";
    public static final int RESULT_DELETED = 2;

    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private TextView tvEmptyList;
    private RecyclerView rvContacts;
    private Button btnAddContact;

    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;

    // ActivityResultLauncher cho màn hình Thêm liên hệ
    private final ActivityResultLauncher<Intent> addContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact newContact = (Contact) result.getData().getSerializableExtra(EXTRA_CONTACT);
                    if (newContact != null) {
                        contactList.add(newContact);
                        contactAdapter.notifyItemInserted(contactList.size() - 1);
                        updateStatistics();
                        checkEmptyList();
                        Toast.makeText(this, R.string.msg_contact_added, Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    // ActivityResultLauncher cho màn hình Chi tiết liên hệ
    private final ActivityResultLauncher<Intent> detailContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    int position = result.getData().getIntExtra(EXTRA_POSITION, -1);
                    if (result.getResultCode() == RESULT_OK) {
                        // Cập nhật trạng thái liên hệ (ví dụ: đổi trạng thái yêu thích)
                        Contact updatedContact = (Contact) result.getData().getSerializableExtra(EXTRA_CONTACT);
                        if (updatedContact != null && position >= 0 && position < contactList.size()) {
                            contactList.set(position, updatedContact);
                            contactAdapter.notifyItemChanged(position);
                            updateStatistics();
                        }
                    } else if (result.getResultCode() == RESULT_DELETED) {
                        // Xóa liên hệ
                        if (position >= 0 && position < contactList.size()) {
                            contactList.remove(position);
                            contactAdapter.notifyItemRemoved(position);
                            contactAdapter.notifyItemRangeChanged(position, contactList.size() - position);
                            updateStatistics();
                            checkEmptyList();
                            Toast.makeText(this, R.string.msg_contact_deleted, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSampleData();
        setupRecyclerView();
        setupListeners();
        updateStatistics();
    }

    private void initViews() {
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        rvContacts = findViewById(R.id.rvContacts);
        btnAddContact = findViewById(R.id.btnAddContact);
    }

    /**
     * Khởi tạo dữ liệu mẫu theo đúng yêu cầu đề bài
     */
    private void initSampleData() {
        contactList = new ArrayList<>();
        contactList.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "Bạn học cùng lớp đại học"));
        contactList.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối"));
        contactList.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Khác", false, "Đồng nghiệp cùng nhóm dự án"));
    }

    private void setupRecyclerView() {
        contactAdapter = new ContactAdapter(this, contactList, (contact, position) -> {
            // Mở màn hình chi tiết liên hệ
            Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
            intent.putExtra(EXTRA_CONTACT, contact);
            intent.putExtra(EXTRA_POSITION, position);
            detailContactLauncher.launch(intent);
        });
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactAdapter);
        checkEmptyList();
    }

    private void setupListeners() {
        btnAddContact.setOnClickListener(v -> {
            // Chuẩn bị danh sách số điện thoại hiện có để kiểm tra trùng lặp
            ArrayList<String> existingPhones = new ArrayList<>();
            for (Contact c : contactList) {
                existingPhones.add(c.getPhone());
            }

            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            intent.putStringArrayListExtra(EXTRA_EXISTING_PHONES, existingPhones);
            addContactLauncher.launch(intent);
        });
    }

    /**
     * Cập nhật các số liệu thống kê: Tổng liên hệ và Số liên hệ yêu thích
     */
    private void updateStatistics() {
        int total = contactList.size();
        int favorites = 0;
        for (Contact c : contactList) {
            if (c.isFavorite()) {
                favorites++;
            }
        }
        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favorites));
    }

    private void checkEmptyList() {
        if (contactList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvContacts.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvContacts.setVisibility(View.VISIBLE);
        }
    }
}

