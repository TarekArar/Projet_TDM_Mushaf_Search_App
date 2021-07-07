package com.mazrou.boilerplate.model.ui

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mazrou.boilerplate.model.database.World

data class Racine(
    val id : String,
    val racine : String   ,
    val letterNumber : String   ,
    val worldNumber : String
)
