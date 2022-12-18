package com.example.micropet.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "pets")
data class PetModel(
    @PrimaryKey(autoGenerate = true)
    val Id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imageId")
    val imageId: Int,
    @ColumnInfo(name = "stats 1")
    var stats1: Int,
    @ColumnInfo(name = "stats 2")
    var stats2: Int,
    @ColumnInfo(name = "stats 3")
    var stats3: Int
):java.io.Serializable
