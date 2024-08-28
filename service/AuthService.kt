package com.yucox.pillpulseapiv1.service

import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Service

@Service
class AuthService(private val firebaseAuth: FirebaseAuth) {
    fun checkMail(mail: String?): String? {
        val user = firebaseAuth.getUserByEmail(mail).email
        return user
    }
}