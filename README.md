# ✅ Ứng dụng Quản Lý Công Việc Cá Nhân

Ứng dụng Android giúp người dùng **quản lý mục tiêu cá nhân**, chia nhỏ thành các nhiệm vụ cụ thể, theo dõi tiến độ và nhắc nhở đúng thời điểm.

---

## 🧠 Mục tiêu của ứng dụng

Giúp người dùng xây dựng thói quen làm việc hiệu quả hơn bằng cách:
- Lập kế hoạch mục tiêu rõ ràng
- Chia nhỏ thành các nhiệm vụ
- Theo dõi và cập nhật tiến độ
- Nhắc nhở đúng thời gian để không bỏ lỡ

---

## 🚀 Tính năng chính

- 🎯 **Tạo và quản lý mục tiêu cá nhân**  
  Ví dụ: Học tiếng Anh, Rèn luyện sức khỏe, Đọc sách,...

- 🗂️ **Tạo và quản lý các nhiệm vụ cụ thể**  
  Ví dụ: Học tiếng Anh → Buổi 1, Buổi 2, Buổi 3,...

- ⏰ **Đặt thông báo nhắc nhở theo từng buổi**  
  Ví dụ: 7:00 sáng → “Học tiếng Anh – Buổi 1”

- 📈 **Tự động cập nhật tiến độ của mục tiêu**  
  Khi bạn đánh dấu một nhiệm vụ hoàn thành, tiến độ tổng thể của mục tiêu sẽ được cập nhật theo phần trăm.

---

## 🧰 Công nghệ sử dụng

- 🔤 **Ngôn ngữ:** Kotlin, XML  
- 🏗️ **Kiến trúc:** MVVM  
- 🗄️ **Lưu trữ:** SQLite (Room)  
- 🔔 **Thông báo:** Notification Manager  
- 🛠️ **IDE:** Android Studio

---

## 📦 Cấu trúc thư mục

```plaintext
📁 DangNhap/                 → Đăng nhập & đăng ký
📁 DatabaseHelper/           → Lớp hỗ trợ thao tác SQLite
📁 home/                     → Màn hình chính
📁 tasks/                    → Danh sách nhiệm vụ
📁 setting/                  → Cài đặt ứng dụng
📁 profile/                  → Thông tin người dùng
📁 ThongBao/                 → Quản lý thông báo
📄 MainActivity.kt           → Màn hình điều hướng chính
```
## 📌 Ghi chú
Ứng dụng chạy hoàn toàn offline

Dễ dàng mở rộng với đồng bộ đám mây hoặc tài khoản người dùng

Giao diện tối giản, dễ sử dụng
