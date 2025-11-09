package com.hoaki.food.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
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
    onFabClick: () -> Unit, // Changed
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
            ProfileScreenBottomBar(
                fabSize = fabSize,
                fabYOffset = fabYOffset,
                onHomeClick = onHomeClick,
                onCartClick = onCartClick,
                onFavoritesClick = onFavoritesClick, // Changed
                onProfileClick = {} // Already on profile screen
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick, // Changed
                containerColor = Color.White,
                contentColor = Color.DarkGray,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .size(fabSize)
                    .offset(y = fabYOffset),
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
                    onClick = { /* TODO */ }
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

// --- Copied from HomeScreen1.kt ---

private class ProfileScreenBottomAppBarShape(
    private val fabRadius: Float,
    private val fabYOffset: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cutoutRadius = fabRadius + 5.dp.toPx(density)
            val cutoutWidth = cutoutRadius * 2
            val cutoutDepth = fabYOffset + fabRadius * 0.5f
            val cornerRadius = 24.dp.toPx(density)

            val center = size.width / 2f
            val cutoutStart = center - cutoutWidth / 2
            val cutoutEnd = center + cutoutWidth / 2

            moveTo(0f, cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, 0f, 2 * cornerRadius, 2 * cornerRadius),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cutoutStart - cornerRadius, 0f)
            cubicTo(cutoutStart, 0f, cutoutStart + cornerRadius * 0.5f, cutoutDepth, center, cutoutDepth)
            cubicTo(cutoutEnd - cornerRadius * 0.5f, cutoutDepth, cutoutEnd, 0f, cutoutEnd + cornerRadius, 0f)
            lineTo(size.width - cornerRadius, 0f)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(size.width - 2 * cornerRadius, 0f, size.width, 2 * cornerRadius),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }

    private fun Dp.toPx(density: Density) = with(density) { toPx() }
}

@Composable
private fun ProfileScreenBottomBar(
    fabSize: Dp,
    fabYOffset: Dp,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit // Changed
) {
    val fabRadius = with(LocalDensity.current) { (fabSize / 2).toPx() }
    val fabYOffsetPx = with(LocalDensity.current) { fabYOffset.toPx() }

    BottomAppBar(
        modifier = Modifier
            .height(65.dp)
            .clip(ProfileScreenBottomAppBarShape(fabRadius = fabRadius, fabYOffset = fabYOffsetPx)),
        containerColor = Color(0xFFEA4C5C),
        contentColor = Color.White,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileScreenBottomNavItem(
                iconRes = R.drawable.home,
                label = "Trang chủ",
                isSelected = false, // Not selected
                onClick = onHomeClick
            )

            ProfileScreenBottomNavItem(
                iconRes = R.drawable.user,
                label = "Tài khoản",
                isSelected = true, // SELECTED!
                onClick = onProfileClick
            )

            Spacer(modifier = Modifier.width(fabSize))

            ProfileScreenBottomNavItem(
                iconRes = R.drawable.mess,
                label = "Giỏ hàng",
                isSelected = false,
                onClick = onCartClick
            )

            ProfileScreenBottomNavItem(
                iconRes = R.drawable.heart,
                label = "Yêu thích", // Changed
                isSelected = false,
                onClick = onFavoritesClick // Changed
            )
        }
    }
}

@Composable
private fun ProfileScreenBottomNavItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(28.dp)
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
