package com.yucox.pillpulseapiv1.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.*

@Document(collection = "Pills")
data class Pill(
    @Id val _id: ObjectId = ObjectId(),
    var id: String = "",
    val drugName: String = "",
    val whenYouTookHour: String? = "",
    val whenYouTookDate: String? = "",
    val month: String? = null,
    var userMail: String = ""
) : Serializable