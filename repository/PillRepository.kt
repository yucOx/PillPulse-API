package com.yucox.pillpulseapiv1.repository

import com.yucox.pillpulseapiv1.model.Pill
import org.springframework.data.mongodb.repository.MongoRepository

interface PillRepository : MongoRepository<Pill, String> {
}