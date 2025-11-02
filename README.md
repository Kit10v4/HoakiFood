# HoakiFood - Food Delivery Android App

Ứng dụng đặt đồ ăn được xây dựng với Jetpack Compose và kiến trúc MVVM.

## Tính năng

- ✅ Đăng nhập & Đăng ký
- ✅ Trang chủ với danh mục món ăn
- ✅ Chi tiết món ăn
- ✅ Giỏ hàng
- ✅ Thanh toán
- ✅ Lịch sử đơn hàng
- ✅ Tìm kiếm món ăn
- ✅ Yêu thích món ăn
- ✅ Hồ sơ người dùng

## Công nghệ sử dụng

- **Jetpack Compose** - UI hiện đại
- **Room Database** - Lưu trữ dữ liệu local
- **Hilt** - Dependency Injection
- **Navigation Component** - Điều hướng
- **Kotlin Coroutines & Flow** - Xử lý bất đồng bộ
- **Material Design 3** - Thiết kế giao diện
- **Coil** - Tải hình ảnh
- **DataStore** - Lưu preferences

## Cấu trúc dự án

```
app/
├── data/
│   ├── local/
│   │   ├── dao/          # Data Access Objects
│   │   └── HoakiFoodDatabase.kt
│   ├── model/            # Data classes
│   ├── preferences/      # DataStore
│   └── repository/       # Repository layer
├── di/                   # Dependency Injection
├── navigation/           # Navigation setup
├── ui/
│   ├── screens/          # Composable screens
│   ├── theme/            # Theme configuration
│   └── viewmodel/        # ViewModels
└── MainActivity.kt
```

## Cách chạy

1. Mở project trong Android Studio
2. Sync Gradle
3. Chạy ứng dụng trên emulator hoặc thiết bị thật

## Yêu cầu

- Android Studio Hedgehog | 2023.1.1+
- Kotlin 1.9.20+
- Android SDK 24+
- JDK 17

## Tác giả

HoakiFood Team
