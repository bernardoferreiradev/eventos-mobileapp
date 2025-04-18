package com.example.eventospt.helpers

import android.content.Context
import android.content.SharedPreferences

fun saveUserDataToCache(context: Context, userName: String, userEmail: String, apelido: String, dataNascimento: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserCache", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("userName", userName)
        putString("userEmail", userEmail)
        putString("apelido", apelido)
        putString("dataNascimento", dataNascimento)
        apply()
    }
}

fun loadUserDataFromCache(context: Context): Map<String, String?> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserCache", Context.MODE_PRIVATE)
    return mapOf(
        "userName" to sharedPreferences.getString("userName", null),
        "userEmail" to sharedPreferences.getString("userEmail", null),
        "apelido" to sharedPreferences.getString("apelido", null),
        "dataNascimento" to sharedPreferences.getString("dataNascimento", null)
    )
}

fun clearUserCache(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserCache", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}
