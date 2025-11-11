package com.hoaki.food.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hoaki.food.R
import com.hoaki.food.ui.viewmodel.AuthViewModel
import com.hoaki.food.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen1(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                onRegisterSuccess()
                viewModel.resetAuthState()
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 120.dp, bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_spoon_fork),
                    contentDescription = "Logo",
                    modifier = Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "HOAKY",
                        fontSize = 30.sp,
                    )
                    Text(
                        text = "FOOD",
                        fontSize = 30.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                Text(
                    text = "Email",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập email") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Số điện thoại",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập số điện thoại") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Mật khẩu",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập mật khẩu") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val iconRes = if (passwordVisible) R.drawable.eye else R.drawable.eyeslash
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Nhập lại mật khẩu",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập lại mật khẩu") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val iconRes = if (confirmPasswordVisible) R.drawable.eye else R.drawable.eyeslash
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (password == confirmPassword && phoneNumber.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                                viewModel.register(email, password, phoneNumber)
                            } else {
                                snackbarHostState.showSnackbar("Mật khẩu không khớp hoặc thông tin còn trống.")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF21BE56)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40.dp),
                    enabled = phoneNumber.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && email.isNotEmpty()
                ) {
                    Text(
                        text = "Đăng ký",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Đã có tài khoản?",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Đăng nhập",
                        fontSize = 15.sp,
                        color = Color(0xFF21BE56),
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}
