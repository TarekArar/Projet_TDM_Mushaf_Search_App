package com.mazrou.boilerplate.network.network_response
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse(

    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("username")
    @Expose
    var username: String ,
    // maybe we can return all the user object here
)
{
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$errorMessage', token='$token', pk=$pk, username='$username')"
    }
}