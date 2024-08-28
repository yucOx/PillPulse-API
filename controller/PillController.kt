package com.yucox.pillpulseapiv1.controller

import com.yucox.pillpulseapiv1.model.Pill
import com.yucox.pillpulseapiv1.service.AuthService
import com.yucox.pillpulseapiv1.service.PillService
import com.yucox.pillpulseapiv1.service.RateLimitingService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pillpulse/api/v1")
class PillController(
    private val rateLimitingService: RateLimitingService,
    private val authService: AuthService,
    private val pillService: PillService
) {
    private val logger = LoggerFactory.getLogger(PillController::class.java)

    @GetMapping()
    fun getPaginatedPillList(
        @RequestHeader("mail", required = true) mail: String,
        @RequestParam("requestedMonth", required = true) requestedMonth: Int,
        @RequestParam("page", required = true) page: Int,
        @RequestParam("limit", required = true) limit: Int,
    ): ResponseEntity<List<Pill>?> {
        if (authService.checkMail(mail).isNullOrBlank()) {
            logger.warn("Unauthorized access attempt: $mail")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val probe = rateLimitingService.checkRate(mail)
        if (!probe.isConsumed) {
            logger.warn("Rate limit exceeded for: $mail")
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null)
        }

        val result = pillService.getPaginatedPillList(mail, requestedMonth, page, limit)
        if (result.isNullOrEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @GetMapping("/all")
    fun getAllPills(@RequestHeader("mail", required = true) mail: String): ResponseEntity<List<Pill>?> {
        if (authService.checkMail(mail).isNullOrBlank()) {
            logger.warn("Unauthorized access attempt: $mail")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        val probe = rateLimitingService.checkRate(mail)
        if (!probe.isConsumed) {
            logger.warn("Rate limit exceeded for: $mail")
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null)
        }

        val result = pillService.getAllPills(mail)
        if (result.isNullOrEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        return ResponseEntity.ok().body(result)
    }

    @PostMapping("/save")
    fun savePill(
        @RequestHeader("mail", required = true) mail: String,
        @RequestBody(required = true) pill: Pill
    ): ResponseEntity<Any> {
        if (authService.checkMail(mail).isNullOrBlank()) {
            logger.warn("Unauthorized access attempt: $mail")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access")
        }
        val probe = rateLimitingService.checkRate(mail)
        if (!probe.isConsumed) {
            logger.warn("Rate limit exceeded for: $mail")
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded")
        }

        pillService.savePill(mail, pill)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping
    fun deletePill(
        @RequestHeader("mail", required = true) mail: String,
        @RequestParam("id", required = true) id: String
    ): ResponseEntity<Any> {
        if (authService.checkMail(mail).isNullOrBlank()) {
            logger.warn("Unauthorized access attempt: $mail")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access")
        }
        val probe = rateLimitingService.checkRate(mail)
        if (!probe.isConsumed) {
            logger.warn("Rate limit exceeded for: $mail")
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded")
        }

        pillService.deletePill(id, mail)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

}