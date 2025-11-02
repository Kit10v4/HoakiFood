package com.hoaki.food.data.repository

import com.hoaki.food.data.local.dao.UserDao
import com.hoaki.food.data.model.User
import com.hoaki.food.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) {
    fun getUserById(userId: Long): Flow<User?> = userDao.getUserById(userId)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = userDao.login(email, password)
            if (user != null) {
                userPreferences.saveUserSession(user.id, user.email, user.fullName)
                Result.success(user)
            } else {
                Result.failure(Exception("Email hoặc mật khẩu không đúng"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(email: String, password: String, fullName: String, phone: String): Result<User> {
        return try {
            // Check if email already exists
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("Email đã được sử dụng"))
            }
            
            val newUser = User(
                email = email,
                password = password,
                fullName = fullName,
                phone = phone
            )
            val userId = userDao.insertUser(newUser)
            val user = newUser.copy(id = userId)
            userPreferences.saveUserSession(user.id, user.email, user.fullName)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        userPreferences.clearUserSession()
    }
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    fun isLoggedIn(): Flow<Boolean> = userPreferences.isLoggedInFlow
    
    fun getCurrentUserId(): Flow<Long?> = userPreferences.userIdFlow
}
