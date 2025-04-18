package com.example.eventospt.helpers

import android.widget.EditText
import android.widget.ImageView
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.example.eventospt.R

fun alterarVisibilidadePassword(
        editTextInputPassword: EditText,
        togglePasswordVisibility: ImageView,
        isPasswordVisible: Boolean
    ): Boolean {
        val newIsPasswordVisible = !isPasswordVisible // Altera o estado da visibilidade

        if (newIsPasswordVisible) {
            // Se a palavra-passe for visível, mostra a palavra-passe
            editTextInputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            togglePasswordVisibility.setImageResource(R.drawable.show_on) // Muda o ícone para "mostrar"
        } else {
            // Se a palavra-passe for oculta, esconde a palavra-passe
            editTextInputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePasswordVisibility.setImageResource(R.drawable.show_off) // Muda o ícone para "esconder"
        }

        editTextInputPassword.setSelection(editTextInputPassword.text.length) // Coloca o cursor no final da palavra-passe
        return newIsPasswordVisible
    }

