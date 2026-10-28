package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydanhba.R;

/**
 * Màn hình Đăng nhập (LoginActivity).
 * Kiểm tra tài khoản cố định (admin / 123456).
 * Khi đăng nhập thành công, chuyển sang MainActivity và gọi finish() để không quay lại.
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định theo yêu cầu đề bài
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123456";

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra bỏ trống
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tvErrorMessage.setText(R.string.login_error_empty);
            tvErrorMessage.setVisibility(View.VISIBLE);
            return;
        }

        // Kiểm tra thông tin đăng nhập với tài khoản cố định
        if (DEFAULT_USERNAME.equals(username) && DEFAULT_PASSWORD.equals(password)) {
            tvErrorMessage.setVisibility(View.GONE);
            // Đăng nhập thành công: chuyển sang MainActivity và finish()
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Sai tên đăng nhập hoặc mật khẩu
            tvErrorMessage.setText(R.string.login_error_invalid);
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}

