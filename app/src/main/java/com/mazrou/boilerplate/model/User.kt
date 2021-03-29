package com.mazrou.boilerplate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val USER_ID = 0
@Entity(tableName = "user")
data class User(
  @Expose
  @SerializedName("username")
  val username : String? = null ,
  @Expose
  @SerializedName("token")
  val token : String? = null
){
  @PrimaryKey(autoGenerate = false)
  val id = USER_ID
}