package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.model.Contact;

import java.util.ArrayList;

/**
 * Màn hình Thêm liên hệ (AddContactActivity).
 * Cho phép người dùng nhập thông tin liên hệ mới, kiểm tra tính hợp lệ và trả về cho MainActivity.
 */
public class AddContactActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText edtName;
    private EditText edtPhone;
    private Spinner spnGroup;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbOther;
    private CheckBox cbFavorite;
    private EditText edtNote;
    private Button btnSave;

    private ArrayList<String> existingPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initViews();
        setupSpinner();
        loadExistingPhones();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        spnGroup = findViewById(R.id.spnGroup);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        cbFavorite = findViewById(R.id.cbFavorite);
        edtNote = findViewById(R.id.edtNote);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.contact_groups,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spnGroup.setAdapter(adapter);
    }

    private void loadExistingPhones() {
        existingPhones = getIntent().getStringArrayListExtra(MainActivity.EXTRA_EXISTING_PHONES);
        if (existingPhones == null) {
            existingPhones = new ArrayList<>();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> handleSaveContact());
    }

    private void handleSaveContact() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        boolean isFavorite = cbFavorite.isChecked();

        // 1. Kiểm tra họ và tên không được để trống
        if (TextUtils.isEmpty(name)) {
            edtName.setError(getString(R.string.error_name_empty));
            edtName.requestFocus();
            return;
        }

        // 2. Kiểm tra số điện thoại không được để trống
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError(getString(R.string.error_phone_empty));
            edtPhone.requestFocus();
            return;
        }

        // 3. Kiểm tra số điện thoại phải đủ 10 chữ số và bắt đầu bằng số 0
        if (!phone.matches("^0[0-9]{9}$")) {
            edtPhone.setError(getString(R.string.error_phone_invalid));
            edtPhone.requestFocus();
            return;
        }

        // 4. Kiểm tra số điện thoại đã tồn tại trong danh bạ chưa
        if (existingPhones.contains(phone)) {
            edtPhone.setError(getString(R.string.error_phone_duplicate));
            edtPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_duplicate, Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy nhóm từ Spinner
        String group = spnGroup.getSelectedItem() != null ? spnGroup.getSelectedItem().toString() : "Khác";

        // Lấy giới tính từ RadioGroup
        String gender = getString(R.string.gender_male);
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rbFemale) {
            gender = getString(R.string.gender_female);
        } else if (selectedGenderId == R.id.rbOther) {
            gender = getString(R.string.gender_other);
        }

        // Tạo đối tượng liên hệ mới
        Contact newContact = new Contact(name, phone, group, gender, isFavorite, note);

        // Trả kết quả về cho MainActivity qua Intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.EXTRA_CONTACT, newContact);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

