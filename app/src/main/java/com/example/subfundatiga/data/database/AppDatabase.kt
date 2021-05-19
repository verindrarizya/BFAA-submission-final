package com.example.subfundatiga.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.subfundatiga.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userFavDao(): UserFavDao

    // for instanstiating and following singleton
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempsInstance = INSTANCE
            if (tempsInstance != null) {
                return tempsInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}