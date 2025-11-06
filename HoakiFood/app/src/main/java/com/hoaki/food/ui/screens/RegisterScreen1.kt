package com.hoaki.food.ui.screens

import com.hoaki.food.navigation.Screen
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
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
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authState by viewModel.authState.collectAsState()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onRegisterSuccess()
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Số điện thoại",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .width(70.dp)
                        .height(50.dp),
                    placeholder = { Text("+84") },
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    placeholder = { Text("Số điện thoại") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Mật khẩu",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
            Spacer(modifier = Modifier.height(7.dp))



            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { 
                    if (password == confirmPassword && phoneNumber.isNotEmpty() && password.isNotEmpty()) {
                        // Sử dụng số điện thoại làm email và fullName mặc định
                        val defaultEmail = "${phoneNumber}@hoakifood.com"
                        val defaultFullName = "User $phoneNumber"
                        viewModel.register(defaultEmail, password, defaultFullName, phoneNumber)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF21BE56)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(40.dp)
            ) {
                Text(
                    text = "Đăng ký",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
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
                Spacer(modifier = Modifier.width(2.dp))
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