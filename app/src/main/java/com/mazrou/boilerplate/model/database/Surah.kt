package com.mazrou.boilerplate.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "surah")
data class Surah(
    @Expose
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id : String,

    @Expose
    @SerializedName("NomSourat")
    @ColumnInfo(name = "surahName")
    val surahName : String  ,

    @Expose
    @SerializedName("Location")
    @ColumnInfo(name = "location")
    val location : String  ,

    @Expose
    @SerializedName("NbrAyat")
    @ColumnInfo(name = "ayatNumber")
    val ayatNumber : Int ,

    @Expose
    @SerializedName("NbrMots")
    @ColumnInfo(name = "worldNumber")
    val worldNumber : Int  ,

    @Expose
    @SerializedName("NbrLettre")
    @ColumnInfo(name = "letterNumber")
    val letterNumber : Int  ,
)


