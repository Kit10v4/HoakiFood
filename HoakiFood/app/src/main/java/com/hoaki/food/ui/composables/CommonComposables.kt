package com.hoaki.food.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoaki.food.data.model.OrderStatus

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.PENDING -> "Đang chờ" to MaterialTheme.colorScheme.onSurface
        OrderStatus.CONFIRMED -> "Đã xác nhận" to MaterialTheme.colorScheme.primary
        OrderStatus.PREPARING -> "Đang chuẩn bị" to MaterialTheme.colorScheme.tertiary
        OrderStatus.DELIVERING -> "Đang giao" to MaterialTheme.colorScheme.tertiary
        OrderStatus.COMPLETED -> "Hoàn thành" to MaterialTheme.colorScheme.secondary
        OrderStatus.CANCELLED -> "Đã hủy" to MaterialTheme.colorScheme.error
    }
    
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}
