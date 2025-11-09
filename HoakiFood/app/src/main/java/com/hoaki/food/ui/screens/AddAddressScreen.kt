package com.hoaki.food.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.ui.viewmodel.AddressViewModel
import com.hoaki.food.ui.viewmodel.SaveAddressState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    addressId: Long? = null,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    var label by remember { mutableStateOf("Home") }
    var fullAddress by remember { mutableStateOf("") }
    var ward by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }
    var showLabelMenu by remember { mutableStateOf(false) }

    val saveState by viewModel.saveState.collectAsState()
    val addresses by viewModel.addresses.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Load existing address if editing
    LaunchedEffect(addressId) {
        if (addressId != null && addressId > 0) {
            val address = addresses.find { it.id == addressId }
            address?.let {
                label = it.label
                fullAddress = it.fullAddress
                ward = it.ward
                district = it.district
                city = it.city
                isDefault = it.isDefault
            }
        }
    }

    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveAddressState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Lưu địa chỉ thành công")
                }
                viewModel.resetSaveState()
                onSaveSuccess()
            }
            is SaveAddressState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar((saveState as SaveAddressState.Error).message)
                }
                viewModel.resetSaveState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (addressId == null || addressId == 0L) "Thêm địa chỉ" else "Sửa địa chỉ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Label Selection
            Text(
                text = "Nhãn địa chỉ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = showLabelMenu,
                onExpandedChange = { showLabelMenu = it }
            ) {
                OutlinedTextField(
                    value = label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nhãn") },
                    leadingIcon = {
                        Icon(
                            imageVector = when (label) {
                                "Home" -> Icons.Default.Home
                                "Work" -> Icons.Default.Work
                                else -> Icons.Default.LocationOn
                            },
                            contentDescription = null
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showLabelMenu) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = showLabelMenu,
                    onDismissRequest = { showLabelMenu = false }
                ) {
                    listOf("Home", "Work", "Other").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                label = option
                                showLabelMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (option) {
                                        "Home" -> Icons.Default.Home
                                        "Work" -> Icons.Default.Work
                                        else -> Icons.Default.LocationOn
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }

            Divider()

            // Address Details
            Text(
                text = "Chi tiết địa chỉ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = fullAddress,
                onValueChange = { fullAddress = it },
                label = { Text("Địa chỉ cụ thể") },
                placeholder = { Text("Số nhà, tên đường") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3
            )

            OutlinedTextField(
                value = ward,
                onValueChange = { ward = it },
                label = { Text("Phường/Xã") },
                placeholder = { Text("Nhập phường/xã") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("Quận/Huyện") },
                placeholder = { Text("Nhập quận/huyện") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Tỉnh/Thành phố") },
                placeholder = { Text("Nhập tỉnh/thành phố") },
                leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            // Default Address Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDefault = !isDefault }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Đặt làm địa chỉ mặc định",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Sử dụng địa chỉ này cho các đơn hàng tiếp theo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isDefault,
                    onCheckedChange = { isDefault = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    if (fullAddress.isNotBlank() && ward.isNotBlank() && 
                        district.isNotBlank() && city.isNotBlank()) {
                        viewModel.saveAddress(
                            id = addressId ?: 0L,
                            label = label,
                            fullAddress = fullAddress,
                            city = city,
                            district = district,
                            ward = ward,
                            isDefault = isDefault
                        )
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Vui lòng điền đầy đủ thông tin")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = saveState !is SaveAddressState.Loading
            ) {
                if (saveState is SaveAddressState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Lưu địa chỉ")
                }
            }
        }
    }
}
