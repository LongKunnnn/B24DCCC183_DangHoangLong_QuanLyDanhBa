# Ứng Dụng Quản Lý Danh Bạ (Contact Management Android App)

Ứng dụng Android quản lý danh bạ cá nhân được phát triển bằng **Java** và **XML**, tuân thủ mô hình kiến trúc chuẩn của Android với 4 màn hình chính: Đăng nhập, Danh sách liên hệ, Thêm liên hệ và Chi tiết liên hệ.

---

## Các màn hình chính

1. **LoginActivity (Đăng nhập)**:
   - Tài khoản mặc định: `admin` / `123456`.
   - Kiểm tra bỏ trống và sai thông tin.
   - Chuyển sang `MainActivity` và gọi `finish()` để không quay lại màn hình đăng nhập.
2. **MainActivity (Danh sách liên hệ & Thống kê)**:
   - Khối thống kê tự động tính tổng số liên hệ và số liên hệ yêu thích.
   - Khởi tạo sẵn 3 dữ liệu mẫu ban đầu: Nguyễn Văn An, Trần Thị Bình (yêu thích), Lê Minh Châu.
   - Danh sách RecyclerView thẻ bo góc với avatar chữ cái đầu, họ tên, số điện thoại, nhãn nhóm phân loại và ngôi sao yêu thích.
   - Nút "+ Thêm liên hệ" và thao tác chạm vào item để xem chi tiết.
3. **AddContactActivity (Thêm liên hệ)**:
   - Các trường nhập: Họ và tên, Số điện thoại, Nhóm (Spinner), Giới tính (RadioGroup), Yêu thích (CheckBox), Ghi chú (nhiều dòng).
   - Kiểm tra hợp lệ: bắt buộc nhập tên; số điện thoại đúng 10 số bắt đầu bằng 0; kiểm tra không trùng lặp số điện thoại đã có.
4. **ContactDetailActivity (Chi tiết liên hệ)**:
   - Hiển thị đầy đủ thông tin chi tiết của người liên hệ.
   - Gọi điện thoại bằng Intent ngầm định (`Intent.ACTION_DIAL`).
   - Đổi trạng thái yêu thích trực tiếp và đồng bộ ngược về danh sách.
   - Xóa liên hệ với hộp thoại `AlertDialog` xác nhận an toàn.

---

## Yêu cầu cài đặt & Khởi chạy

- **Công cụ**: Android Studio (Flamingo trở lên).
- **Ngôn ngữ**: Java (JDK 17).
- **Compile SDK**: 34, **Min SDK**: 24.
- Mở thư mục dự án trong Android Studio, chờ Gradle Sync hoàn tất, sau đó nhấn **Run ▶** (Shift + F10) trên máy ảo hoặc thiết bị thật.

  **Xem hướng dẫn chi tiết và tài liệu làm báo cáo tại file**:
  HUONG DAN SU DUNG.docx
