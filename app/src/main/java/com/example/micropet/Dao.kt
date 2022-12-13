package com.example.micropet
import android.icu.text.Transliterator.Position
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    fun insertPet(item: PetModel)
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetModel>>

}