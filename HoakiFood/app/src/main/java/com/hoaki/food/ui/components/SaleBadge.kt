package com.hoaki.food.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoaki.food.ui.theme.BadgeRed
import com.hoaki.food.ui.theme.TextWhite

/**
 * Sale Badge Component
 * Hiển thị phần trăm giảm giá (ví dụ: "12% OFF")
 * 
 * @param discount Phần trăm giảm giá (0-100)
 * @param modifier Modifier cho badge
 */
@Composable
fun SaleBadge(
    discount: Int,
    modifier: Modifier = Modifier
) {
    if (discount > 0) {
        Box(
            modifier = modifier
                .background(
                    color = BadgeRed,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$discount% OFF",
                color = TextWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Price Display Component with Discount
 * Hiển thị giá gốc (gạch ngang) và giá sau giảm
 * 
 * @param originalPrice Giá gốc
 * @param discount Phần trăm giảm giá (0-100)
 * @param modifier Modifier
 */
@Composable
fun PriceWithDiscount(
    originalPrice: Double,
    discount: Int,
    modifier: Modifier = Modifier
) {
    val finalPrice = if (discount > 0) {
        originalPrice * (1 - discount / 100.0)
    } else {
        originalPrice
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        if (discount > 0) {
            // Giá gốc - gạch ngang
            Text(
                text = formatPrice(originalPrice),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
            )
        }
        
        // Giá sau giảm hoặc giá gốc
        Text(
            text = formatPrice(finalPrice),
            style = MaterialTheme.typography.titleMedium,
            color = if (discount > 0) BadgeRed else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Format price to VND currency
 */
private fun formatPrice(price: Double): String {
    return "${price.toInt().toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1.")}₫"
}
