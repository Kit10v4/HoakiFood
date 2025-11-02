# Hướng dẫn sử dụng HoakiFood App

## 📋 Tổng quan

HoakiFood là ứng dụng đặt đồ ăn hoàn chỉnh được xây dựng bằng Android Kotlin với Jetpack Compose. Ứng dụng sử dụng kiến trúc MVVM và các thư viện hiện đại nhất.

## 🎯 Tính năng đã implement

### 1. **Authentication (Xác thực)**

- ✅ Đăng nhập với email và mật khẩu
- ✅ Đăng ký tài khoản mới
- ✅ Lưu session người dùng với DataStore
- ✅ Tự động đăng nhập lại khi mở app

### 2. **Trang chủ (Home)**

- ✅ Hiển thị danh sách danh mục món ăn
- ✅ Món ăn phổ biến (Popular Foods)
- ✅ Tất cả món ăn với filter theo category
- ✅ Badge hiển thị số lượng sản phẩm trong giỏ hàng
- ✅ Rating và review count cho mỗi món

### 3. **Chi tiết món ăn (Food Detail)**

- ✅ Hiển thị hình ảnh món ăn
- ✅ Mô tả chi tiết
- ✅ Chọn số lượng
- ✅ Thêm vào giỏ hàng
- ✅ Đánh dấu yêu thích
- ✅ Hiển thị thời gian chuẩn bị và calories

### 4. **Giỏ hàng (Cart)**

- ✅ Hiển thị danh sách món đã chọn
- ✅ Tăng/giảm số lượng
- ✅ Xóa món khỏi giỏ
- ✅ Tính tổng tiền tự động
- ✅ Phí giao hàng
- ✅ Xóa tất cả giỏ hàng

### 5. **Thanh toán (Checkout)**

- ✅ Nhập địa chỉ giao hàng
- ✅ Nhập số điện thoại
- ✅ Ghi chú đơn hàng (optional)
- ✅ Tóm tắt đơn hàng
- ✅ Tạo đơn hàng mới

### 6. **Đơn hàng (Orders)**

- ✅ Lịch sử đơn hàng
- ✅ Chi tiết đơn hàng
- ✅ Trạng thái đơn hàng (Pending, Confirmed, Preparing, Delivering, Completed, Cancelled)
- ✅ Mã đơn hàng tự động

### 7. **Tìm kiếm (Search)**

- ✅ Tìm kiếm món ăn theo tên
- ✅ Tìm kiếm theo mô tả
- ✅ Hiển thị kết quả real-time

### 8. **Yêu thích (Favorites)**

- ✅ Danh sách món ăn yêu thích
- ✅ Toggle favorite/unfavorite

### 9. **Hồ sơ (Profile)**

- ✅ Thông tin người dùng
- ✅ Lịch sử đơn hàng
- ✅ Món ăn yêu thích
- ✅ Đăng xuất

## 🏗️ Kiến trúc

### **MVVM (Model-View-ViewModel)**

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  (Composable Screens & Components)      │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│          ViewModel Layer                │
│  (Business Logic & State Management)    │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│         Repository Layer                │
│  (Data Access Abstraction)              │
└─────────────┬───────────────────────────┘
              │
              ↓
┌─────────────────────────────────────────┐
│          Data Layer                     │
│  (Room Database & DataStore)            │
└─────────────────────────────────────────┘
```

## 📦 Dependencies chính

| Library            | Version    | Mục đích             |
| ------------------ | ---------- | -------------------- |
| Jetpack Compose    | 2023.10.01 | UI Framework         |
| Room               | 2.6.1      | Local Database       |
| Hilt               | 2.48       | Dependency Injection |
| Navigation Compose | 2.7.5      | Navigation           |
| Coil               | 2.5.0      | Image Loading        |
| DataStore          | 1.0.0      | Preferences Storage  |
| Gson               | 2.10.1     | JSON Parsing         |

## 🗄️ Database Schema

### **Tables:**

1. **users**

   - id, email, password, fullName, phone, avatarUrl, createdAt

2. **categories**

   - id, name, description, imageUrl, displayOrder

3. **foods**

   - id, name, description, price, imageUrl, categoryId, ingredients, rating, reviewCount, isPopular, isFavorite, preparationTime, calories

4. **cart_items**

   - id, foodId, foodName, foodPrice, foodImageUrl, quantity, note, userId

5. **orders**
   - id, userId, orderNumber, items (JSON), subtotal, deliveryFee, total, status, deliveryAddress, phoneNumber, note, createdAt, updatedAt

## 🚀 Cách chạy dự án

### **Bước 1: Mở project trong Android Studio**

```
File → Open → Chọn thư mục HoakiFood
```

### **Bước 2: Sync Gradle**

Đợi Android Studio tự động sync dependencies, hoặc:

```
File → Sync Project with Gradle Files
```

### **Bước 3: Chạy ứng dụng**

1. Chọn device/emulator
2. Click "Run" (Shift + F10)

## 📱 Dữ liệu mẫu

App tự động tạo dữ liệu mẫu khi chạy lần đầu:

### **Categories:**

- Món chính
- Món phụ
- Đồ uống
- Tráng miệng
- Món ăn vặt

### **Sample Foods:**

- Phở bò - 45,000đ
- Bún chả - 40,000đ
- Cơm tấm - 35,000đ
- Bánh mì - 20,000đ
- Gỏi cuốn - 25,000đ
- Nem rán - 30,000đ
- Trà đào cam sả - 25,000đ
- Cà phê sữa đá - 20,000đ
- Sinh tố bơ - 30,000đ
- Chè ba màu - 20,000đ

## 🔐 Test Account

Vì app sử dụng local database, bạn cần đăng ký tài khoản mới khi chạy lần đầu.

**Test Registration:**

- Full Name: Test User
- Email: test@hoakifood.com
- Phone: 0123456789
- Password: 123456

## 🎨 UI/UX Features

- ✅ Material Design 3
- ✅ Dark mode support
- ✅ Smooth animations
- ✅ Loading states
- ✅ Error handling
- ✅ Empty states
- ✅ Snackbar notifications
- ✅ Bottom sheet dialogs
- ✅ Card elevations
- ✅ Icon badges

## 🔄 State Management

Sử dụng Kotlin Flow và StateFlow:

```kotlin
val cartItems: StateFlow<List<CartItem>>
val isLoggedIn: StateFlow<Boolean>
val orders: StateFlow<List<Order>>
```

## 📝 Các tính năng có thể mở rộng

### **1. Backend Integration**

- Thay thế Room Database bằng API calls
- Retrofit cho network requests
- JWT authentication

### **2. Payment Integration**

- Momo, ZaloPay, VNPay
- Credit/Debit cards
- COD (Cash on Delivery)

### **3. Real-time Features**

- Firebase Cloud Messaging cho notifications
- Real-time order tracking
- Chat với nhà hàng

### **4. Maps Integration**

- Google Maps cho delivery tracking
- Location picker cho địa chỉ
- Nearby restaurants

### **5. Enhanced Features**

- Review & Rating system
- Promo codes & Discounts
- Loyalty points
- Push notifications
- Social sharing
- Multi-language support

### **6. Analytics**

- Firebase Analytics
- User behavior tracking
- Crash reporting

### **7. Testing**

- Unit tests với JUnit
- UI tests với Compose Testing
- Integration tests

## 🛠️ Troubleshooting

### **Lỗi: "Unresolved reference"**

**Giải pháp:** Sync Gradle lại hoặc Invalidate Caches

### **Lỗi: "KSP not found"**

**Giải pháp:** Đảm bảo KSP plugin đã được thêm trong build.gradle

### **App crash khi mở**

**Giải pháp:**

1. Clean Project (Build → Clean Project)
2. Rebuild Project (Build → Rebuild Project)
3. Uninstall app và chạy lại

### **Database lỗi**

**Giải pháp:**

1. Uninstall app
2. Run lại để tạo database mới

## 📞 Support

Nếu gặp vấn đề, hãy:

1. Check logs trong Logcat
2. Xem lại dependencies trong build.gradle
3. Đảm bảo SDK version phù hợp (minSdk 24, targetSdk 34)

## 🎓 Learning Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## 📄 License

This project is for educational purposes.

---

**Chúc bạn code vui vẻ! 🚀**
