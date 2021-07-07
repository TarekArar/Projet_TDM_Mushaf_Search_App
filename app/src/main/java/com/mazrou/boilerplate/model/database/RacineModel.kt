package com.mazrou.boilerplate.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "racine")
data class RacineModel(
    @Expose
    @SerializedName("idRacine")
    @PrimaryKey(autoGenerate = false)
    val id : String,
    @Expose
    @SerializedName("Racine")
    val racine : String   ,
    @Expose
    @SerializedName("E1")
    val e1 : String   ,
    @Expose
    @SerializedName("E2")
    val e2 : String   ,
    @Expose
    @SerializedName("E3")
    val e3 : String   ,
    @Expose
    @SerializedName("E5")
    val e4 : String   ,
    @Expose
    @SerializedName("E6")
    val e5 : String   ,
    @Expose
    @SerializedName("NBLettre")
    val letterNumber : String   ,
)