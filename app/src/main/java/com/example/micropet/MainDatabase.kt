package com.example.micropet

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PetModel::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
    companion object{
        fun getDatabase(context: Context): MainDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java,
                "DataBase"
            ).build()
        }
    }
}