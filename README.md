# 📱 Ứng dụng Bán Hàng – Android

Ứng dụng bán hàng dành cho Android, hỗ trợ quản lý người dùng, sản phẩm, đơn hàng và tạo hóa đơn nhanh chóng, thân thiện với người dùng. Hoạt động hoàn toàn offline.

---

## 🚀 Tính năng chính

- Đăng nhập và phân quyền người dùng (Admin / Thu ngân)
- Quản lý người dùng: thêm, sửa, xóa
- Quản lý danh mục và sản phẩm
- Tạo đơn hàng sử dụng camera trên điện thoại để quét mã vạch trên mỗi sản phẩm thêm vào đơn hàng
- In hóa đơn qua thiết bị in kết nối qua bluetooth
- Xem báo cáo doanh thu
- Lưu trữ dữ liệu cục bộ bằng Room
- Giao diện hiện đại, dễ sử dụng

---

## 🏗️ Kiến trúc phần mềm

Ứng dụng tuân theo mô hình **Clean Architecture**, kết hợp với kiến trúc **MVVM (Model - View - ViewModel)** giúp code rõ ràng, dễ bảo trì và mở rộng.

### 📁 Cấu trúc thư mục

```plaintext
presentation/
│
├── View (Fragment, Activity) #Giao diện
├── ViewModel #Xử lý tương tác UI
│
domain/
│
├── model/ # Định nghĩa các entity
├── repository/ (interface) #Interface kết nối với data layer
├── use_case/ #xử lý logic chính
│
data/
├── locale/ (Room DAO, Database)
├── repository/ (implementation)

📦 di/                   → Cấu hình Hilt DI
📦 utils/                → Hằng số và hàm tiện ích
📄 MyApplication.kt      → Application class

```
## 🧰 Công nghệ sử dụng

- 🏠 **Room** – Quản lý cơ sở dữ liệu cục bộ
- 💉 **Hilt** – Dependency Injection
- 🔁 **Kotlin Coroutines** – Xử lý bất đồng bộ
- 👓 **Jetpack ViewModel + LiveData** – Quản lý vòng đời và dữ liệu UI

---
## 📌 Ghi chú
Ứng dụng hoạt động hoàn toàn offline

Thiết kế tách biệt UI – Logic – Data

Sẵn sàng tích hợp API hoặc đồng bộ dữ liệu trong tương lai
