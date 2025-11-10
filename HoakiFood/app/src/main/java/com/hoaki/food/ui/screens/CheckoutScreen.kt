package com.hoaki.food.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.data.model.Address
import com.hoaki.food.data.model.CartItem
import com.hoaki.food.ui.formatPrice
import com.hoaki.food.ui.viewmodel.AddressViewModel
import com.hoaki.food.ui.viewmodel.CartViewModel
import com.hoaki.food.ui.viewmodel.CheckoutState
import com.hoaki.food.ui.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onOrderSuccess: (Long) -> Unit,
    onManageAddressClick: () -> Unit,
    cartViewModel: CartViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    addressViewModel: AddressViewModel = hiltViewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var selectedAddressId by remember { mutableStateOf<Long?>(null) }
    
    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal by cartViewModel.subtotal.collectAsState()
    val deliveryFee by cartViewModel.deliveryFee.collectAsState()
    val total by cartViewModel.total.collectAsState()
    val checkoutState by orderViewModel.checkoutState.collectAsState()
    val addresses by addressViewModel.addresses.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Auto-select default address
    LaunchedEffect(addresses) {
        if (selectedAddressId == null) {
            val defaultAddress = addresses.find { it.isDefault }
            selectedAddressId = defaultAddress?.id
        }
    }
    
    val selectedAddress = addresses.find { it.id == selectedAddressId }
    val deliveryAddress = selectedAddress?.let {
        "${it.fullAddress}, ${it.ward}, ${it.district}, ${it.city}"
    } ?: ""
    
    
    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is CheckoutState.Success -> {
                val order = (checkoutState as CheckoutState.Success).order
                onOrderSuccess(order.id)
                orderViewModel.resetCheckoutState()
            }
            is CheckoutState.Error -> {
                snackbarHostState.showSnackbar((checkoutState as CheckoutState.Error).message)
                orderViewModel.resetCheckoutState()
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng cộng",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatPrice(total),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            if (deliveryAddress.isNotBlank() && phoneNumber.isNotBlank()) {
                                orderViewModel.createOrder(
                                    cartItems = cartItems,
                                    deliveryAddress = deliveryAddress,
                                    phoneNumber = phoneNumber,
                                    note = note.ifBlank { null }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = checkoutState !is CheckoutState.Loading && 
                                 deliveryAddress.isNotBlank() && phoneNumber.isNotBlank()
                    ) {
                        if (checkoutState is CheckoutState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Đặt hàng")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Delivery Address
            Card(
                modifier = Modifier.clickable { showAddressDialog = true }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Địa chỉ giao hàng",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = onManageAddressClick) {
                            Text("Quản lý")
                        }
                    }
                    
                    if (selectedAddress != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = selectedAddress.label,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (selectedAddress.isDefault) {
                                        Surface(
                                            shape = MaterialTheme.shapes.small,
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = "Mặc định",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = deliveryAddress,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                TextButton(
                                    onClick = { showAddressDialog = true },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Thay đổi địa chỉ")
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = onManageAddressClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Thêm địa chỉ giao hàng")
                        }
                    }
                    
                    
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            
            // Order Note
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Ghi chú đơn hàng",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Ghi chú (không bắt buộc)") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
            
            // Order Summary
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Tóm tắt đơn hàng",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.foodName}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatPrice(item.foodPrice * item.quantity),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tạm tính",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatPrice(subtotal),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Phí giao hàng",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatPrice(deliveryFee),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        // Address Selection Dialog
        if (showAddressDialog && addresses.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { showAddressDialog = false },
                title = { Text("Chọn địa chỉ giao hàng") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        addresses.forEach { address ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedAddressId = address.id
                                        showAddressDialog = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedAddressId == address.id)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = address.label,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (address.isDefault) {
                                            Surface(
                                                shape = MaterialTheme.shapes.small,
                                                color = MaterialTheme.colorScheme.primary
                                            ) {
                                                Text(
                                                    text = "Mặc định",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${address.fullAddress}, ${address.ward}, ${address.district}, ${address.city}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAddressDialog = false }) {
                        Text("Đóng")
                    }
                }
            )
        }
    }
}