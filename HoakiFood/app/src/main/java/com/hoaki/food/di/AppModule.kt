package com.hoaki.food.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.hoaki.food.R
import com.hoaki.food.data.local.HoakiFoodDatabase
import com.hoaki.food.data.local.dao.*
import com.hoaki.food.data.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Migration from version 1 to 2: Add discount field to foods table
            database.execSQL("ALTER TABLE foods ADD COLUMN discount INTEGER NOT NULL DEFAULT 0")
        }
    }
    
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Migration from version 2 to 3: Add addresses table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `addresses` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `userId` INTEGER NOT NULL,
                    `label` TEXT NOT NULL,
                    `fullAddress` TEXT NOT NULL,
                    `city` TEXT NOT NULL,
                    `district` TEXT NOT NULL,
                    `ward` TEXT NOT NULL,
                    `isDefault` INTEGER NOT NULL
                )
            """)
        }
    }
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HoakiFoodDatabase {
        return Room.databaseBuilder(
            context,
            HoakiFoodDatabase::class.java,
            "hoakifood_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: HoakiFoodDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    @Singleton
    fun provideCategoryDao(database: HoakiFoodDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    @Singleton
    fun provideFoodDao(database: HoakiFoodDatabase): FoodDao {
        return database.foodDao()
    }
    
    @Provides
    @Singleton
    fun provideCartDao(database: HoakiFoodDatabase): CartDao {
        return database.cartDao()
    }
    
    @Provides
    @Singleton
    fun provideOrderDao(database: HoakiFoodDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideAddressDao(database: HoakiFoodDatabase): AddressDao {
        return database.addressDao()
    }
    
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}
