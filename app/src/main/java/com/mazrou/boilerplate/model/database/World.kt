package com.mazrou.boilerplate.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "world",
        foreignKeys = [
            androidx.room.ForeignKey(
                entity = Surah::class,
                parentColumns = ["id"],
                childColumns = ["idSurah"],
                onDelete = androidx.room.ForeignKey.CASCADE
            ) ,
            androidx.room.ForeignKey(
                entity = Ayat::class,
                parentColumns = ["id"],
                childColumns = ["ayatID"],
                onDelete = androidx.room.ForeignKey.CASCADE
            ) ,
            androidx.room.ForeignKey(
                entity = RacineModel::class,
                parentColumns = ["id"],
                childColumns = ["idRacine"],
                onDelete = androidx.room.ForeignKey.CASCADE
            )
        ]
)
data class World  (
    @Expose
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id : Int,

    @Expose
    @SerializedName("ID_Word")
    val wordId : String ,

    @Expose
    @SerializedName("ID_Aya")
    val ayatID : String  ,

    @Expose
    @SerializedName("idSourat")
    val idSurah : String  ,

    @Expose
    @SerializedName("NumAya")
    val ayatNumber : String  ,

    @Expose
    @SerializedName("idRacine")
    val  idRacine : String  ,

    @Expose
    @SerializedName("ArabicWord")
    val arabicWord : String  ,

    @Expose
    @SerializedName("englishWord")
    val  englishWord : String  ,
)