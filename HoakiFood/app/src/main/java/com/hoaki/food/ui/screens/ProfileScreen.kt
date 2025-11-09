package com.hoaki.food.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.R
import com.hoaki.food.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onOrderHistoryClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onFabClick: () -> Unit,
    onAddressClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val userName by authViewModel.userName.collectAsState()
    val userEmail by authViewModel.userEmail.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

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

    val fabSize = 64.dp
    val fabYOffset = 16.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ") }
            )
        },
        bottomBar = {
            Bottom(
                fabSize = fabSize,
                fabYOffset = fabYOffset,
                currentScreen = "profile",
                onHomeClick = onHomeClick,
                onCartClick = onCartClick,
                onFavoritesClick = onFavoritesClick,
                onProfileClick = {} // Already on profile screen
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = Color.White,
                contentColor = Color.DarkGray,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .size(fabSize)
                    .offset(y = fabYOffset), // Đẩy FAB xuống
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bottombut),
                    contentDescription = "Menu",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
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
                                text = userName ?: "Người dùng",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userEmail ?: "HoakiFood Member",
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
                    onClick = onAddressClick
                )
            }

            // Settings
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Cài đặt",
                    onClick = { /* TODO */ }
                )
            }

            // About
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "Về chúng tôi",
                    onClick = { /* TODO */ }
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
    icon: ImageVector,
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
