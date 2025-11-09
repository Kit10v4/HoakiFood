package com.hoaki.food.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hoaki.food.data.local.dao.UserDao
import com.hoaki.food.data.model.User
import com.hoaki.food.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences,
    private val firebaseAuth: FirebaseAuth
) {

    fun getUserById(userId: Long): Flow<User?> = userDao.getUserById(userId)

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                // Find user in local DB or create a new one
                var localUser = userDao.getUserByEmail(email)
                if (localUser == null) {
                    localUser = User(email = email, fullName = firebaseUser.displayName ?: email.substringBefore('@'), phone = firebaseUser.phoneNumber ?: "", password = "")
                    val newUserId = userDao.insertUser(localUser)
                    localUser = localUser.copy(id = newUserId)
                }
                userPreferences.saveUserSession(localUser.id, localUser.email, localUser.fullName)
                Result.success(localUser)
            } else {
                Result.failure(Exception("Đăng nhập Firebase thất bại."))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Email hoặc mật khẩu không đúng."))
        }
    }

    suspend fun register(email: String, password: String, fullName: String, phone: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val newUser = User(
                    email = email,
                    password = "", // Don't store password locally
                    fullName = fullName,
                    phone = phone
                )
                val userId = userDao.insertUser(newUser)
                val user = newUser.copy(id = userId)
                // Do not auto-login, let user log in manually after registration
                Result.success(user)
            } else {
                Result.failure(Exception("Không thể tạo người dùng trên Firebase."))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Đăng ký thất bại."))
        }
    }

    suspend fun logout() {
        firebaseAuth.signOut()
        userPreferences.clearUserSession()
    }

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    fun isLoggedIn(): Flow<Boolean> = userPreferences.isLoggedInFlow

    fun getCurrentUserId(): Flow<Long?> = userPreferences.userIdFlow
}
