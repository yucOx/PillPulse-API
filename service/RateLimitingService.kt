package com.yucox.pillpulseapiv1.service

import com.google.firebase.auth.FirebaseAuth
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.ConsumptionProbe
import io.github.bucket4j.Refill
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitingService() {
    private val buckets = ConcurrentHashMap<String, Bucket>()
    fun createRateLimiter(): Bucket {
        val limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)))
        return Bucket.builder()
            .addLimit(limit)
            .build()
    }

    fun resolveBucket(email: String): Bucket {
        return buckets.computeIfAbsent(email) {
            createRateLimiter()
        }
    }

    fun checkRate(mail: String): ConsumptionProbe {
        val bucket = resolveBucket(mail)
        val probe: ConsumptionProbe = bucket.tryConsumeAndReturnRemaining(1)
        return probe
    }
}