package com.mazrou.boilerplate.model.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "ayat",
    foreignKeys = [
        ForeignKey(
            entity = Surah::class,
            parentColumns = ["id"],
            childColumns = ["idSurah"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ayat(
    @Expose
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id : String ,

    @Expose
    @SerializedName("idSourat")
    val idSurah : Int,

    @Expose
    @SerializedName("NumAya")
    val ayatNumber : Int? = null  ,

    @Expose
    @SerializedName("Text_AR")
    val text : String  ,

    @Expose
    @SerializedName("nbMots")
    val worldNumber : Int ,
)




