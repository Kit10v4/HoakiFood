package com.hoaki.food.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.R
import com.hoaki.food.ui.viewmodel.HomeViewModel

val AppFontFamily = FontFamily(
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_thin, FontWeight.Thin),
    Font(R.font.lobstertwo_bold, FontWeight.Bold),
    Font(R.font.lobstertwo_regular, FontWeight.Normal)
)

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(
                color = Color(0xFFEA4C5C)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 25.dp, end = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_spoon_fork),
                    contentDescription = "Logo",
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HOAKY FOOD",
                        fontSize = 35.sp,
                        color = Color.White,
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Bold,
                        softWrap = false
                    )
                    Text(
                        text = "Nhanh và tiện lợi",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = AppFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "GIAO ĐẾN:",
                color = Color.White.copy(alpha = 0.9f),
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 25.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vitri2),
                    contentDescription = "Location",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "02 Võ Oanh, Thạnh Lộc, Mỹ Tây, Hồ Chí Minh, Việt Nam",
                    color = Color.White,
                    fontFamily = AppFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen1Preview() {
    HomeScreen1(
        onProfileClick = {},
        onCartClick = {},
        onSearchClick = {},
        onFoodClick = {},
        onFabClick = {},
        onFavoritesClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(36.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp),
                clip = false
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (searchText.isEmpty()) {
                Text(
                    text = "Tìm kiếm",
                    fontFamily = AppFontFamily,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                textStyle = TextStyle(
                    fontFamily = AppFontFamily,
                    fontSize = 12.sp,
                    color = Color.Black
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                enabled = false // Disable text field to make the whole bar clickable
            )
        }
    }
}

@Composable
fun HomeScreen1(
    onFoodClick: (Long) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFabClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val allFoods by viewModel.allFoods.collectAsState()
    val fabSize = 64.dp
    val fabYOffset = 16.dp // Điều chỉnh độ sâu của FAB

    Scaffold(
        topBar = {
            Header()
        },
        bottomBar = {
            Bottom(
                fabSize = fabSize,
                fabYOffset = fabYOffset,
                onProfileClick = onProfileClick,
                onCartClick = onCartClick,
                onFavoritesClick = onFavoritesClick
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
        floatingActionButtonPosition = FabPosition.Center,
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(
                            color = Color(0xFFF2F3F7),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryItem(R.drawable.anvat, "Ăn vặt", AppFontFamily)
                        CategoryItem(R.drawable.doan, "Đồ ăn", AppFontFamily)
                        CategoryItem(R.drawable.thucuong, "Thức Uống", AppFontFamily)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(
                            color = Color(0xFFF2F3F7),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryItem(R.drawable.vitri, "Gần bạn", AppFontFamily)
                        CategoryItem(R.drawable.khuyenmai, "Khuyến mãi", AppFontFamily)
                        CategoryItem(R.drawable.tieubieu, "Tiêu biểu", AppFontFamily)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tất cả món ăn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(allFoods) { food ->
                FoodListItem(
                    food = food,
                    onClick = { onFoodClick(food.id) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (190).dp)
                .zIndex(1f)
                .clickable { onSearchClick() },
            contentAlignment = Alignment.Center
        ) {
            SearchBar()
        }
    }
}

@Composable
fun CategoryItem(iconRes: Int, text: String, fontFamily: FontFamily) {
    Box(
        modifier = Modifier
            .size(90.dp, 85.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F8F6))
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier
                    .size(36.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                color = Color.Black,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

private class BottomAppBarShape(
    private val fabRadius: Float,
    private val fabYOffset: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cutoutRadius = fabRadius + 5.dp.toPx(density) // Bán kính của đường cong
            val cutoutWidth = cutoutRadius * 2
            val cutoutDepth = fabYOffset + fabRadius * 0.5f // Độ sâu của đường cong
            val cornerRadius = 24.dp.toPx(density) // Bán kính bo góc

            // Các điểm neo chính
            val center = size.width / 2f
            val cutoutStart = center - cutoutWidth / 2
            val cutoutEnd = center + cutoutWidth / 2

            moveTo(0f, cornerRadius)
            // Góc bo tròn bên trái
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, 0f, 2 * cornerRadius, 2 * cornerRadius),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(cutoutStart - cornerRadius, 0f)

            // Đường cong lõm vào
            cubicTo(
                x1 = cutoutStart,
                y1 = 0f,
                x2 = cutoutStart + cornerRadius * 0.5f,
                y2 = cutoutDepth,
                x3 = center,
                y3 = cutoutDepth
            )
            cubicTo(
                x1 = cutoutEnd - cornerRadius * 0.5f,
                y1 = cutoutDepth,
                x2 = cutoutEnd,
                y2 = 0f,
                x3 = cutoutEnd + cornerRadius,
                y3 = 0f
            )

            lineTo(size.width - cornerRadius, 0f)
            // Góc bo tròn bên phải
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
fun Bottom(
    fabSize: Dp,
    fabYOffset: Dp,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onFavoritesClick: () -> Unit // Changed
) {
    val fabRadius = with(LocalDensity.current) { (fabSize / 2).toPx() }
    val fabYOffsetPx = with(LocalDensity.current) { fabYOffset.toPx() }

    BottomAppBar(
        modifier = Modifier
            .height(65.dp) // Tăng chiều cao để có không gian cho đường cong
            .clip(BottomAppBarShape(fabRadius = fabRadius, fabYOffset = fabYOffsetPx)), // Áp dụng shape mới
        containerColor = Color(0xFFEA4C5C),
        contentColor = Color.White,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                iconRes = R.drawable.home,
                label = "Trang chủ",
                isSelected = true, // For now, Home is always selected
                onClick = onHomeClick
            )

            BottomNavItem(
                iconRes = R.drawable.user,
                label = "Tài khoản",
                isSelected = false,
                onClick = onProfileClick
            )

            Spacer(modifier = Modifier.width(fabSize))

            BottomNavItem(
                iconRes = R.drawable.mess,
                label = "Giỏ hàng",
                isSelected = false,
                onClick = onCartClick // Gắn sự kiện click
            )

            BottomNavItem(
                iconRes = R.drawable.heart,
                label = "Yêu thích", // Changed
                isSelected = false,
                onClick = onFavoritesClick // Changed
            )
        }
    }
}

@Composable
fun BottomNavItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
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
