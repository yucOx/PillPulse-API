package com.yucox.pillpulseapiv1

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileNotFoundException

@Configuration
class FirebaseConfig {
    @Bean
    fun firebaseAuth(): FirebaseAuth {
        val serviceAccount = this.javaClass.getResourceAsStream("/pillpulse-********.json")
            ?: throw FileNotFoundException("Firebase configuration file not found")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
        return FirebaseAuth.getInstance()
    }
}
