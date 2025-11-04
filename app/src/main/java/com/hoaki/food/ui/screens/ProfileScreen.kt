package com.hoaki.food.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onOrderHistoryClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddressesDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc muốn đăng xuất?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        authViewModel.logout()
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text("Đồng ý")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
    
    // Addresses Dialog
    if (showAddressesDialog) {
        AlertDialog(
            onDismissRequest = { showAddressesDialog = false },
            title = { Text("Địa chỉ giao hàng") },
            text = { Text("Tính năng quản lý địa chỉ giao hàng sẽ được cập nhật trong phiên bản sau. Hiện tại bạn có thể nhập địa chỉ trực tiếp khi thanh toán.") },
            confirmButton = {
                TextButton(onClick = { showAddressesDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
    
    // Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Cài đặt") },
            text = { Text("Tính năng cài đặt sẽ được cập nhật trong phiên bản sau. Bạn có thể cài đặt ngôn ngữ, thông báo, và các tùy chọn khác.") },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
    
    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Về chúng tôi") },
            text = { 
                Column {
                    Text("HoakiFood - Ứng dụng đặt đồ ăn")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Phiên bản: 1.0.0")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("© 2024 HoakiFood Team")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ứng dụng được xây dựng với Jetpack Compose và Material Design 3.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header
            item {
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        
                        Column {
                            Text(
                                text = "Người dùng",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "HoakiFood Member",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Menu Section
            item {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            
            // Order History
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Receipt,
                    title = "Lịch sử đơn hàng",
                    onClick = onOrderHistoryClick
                )
            }
            
            // Favorites
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Favorite,
                    title = "Món ăn yêu thích",
                    onClick = onFavoritesClick
                )
            }
            
            // Addresses
            item {
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Địa chỉ giao hàng",
                    onClick = { showAddressesDialog = true }
                )
            }
            
            // Settings
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Cài đặt",
                    onClick = { showSettingsDialog = true }
                )
            }
            
            // About
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "Về chúng tôi",
                    onClick = { showAboutDialog = true }
                )
            }
            
            // Logout
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Đăng xuất",
                    onClick = { showLogoutDialog = true },
                    isDestructive = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
