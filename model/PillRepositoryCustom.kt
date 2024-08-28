package com.yucox.pillpulseapiv1.model

interface PillRepositoryCustom {
    fun getPaginated(mail: String, requestedMonth: Int, page: Int, limit: Int): List<Pill>?
    fun get(mail: String): List<Pill>
    fun getItemCount(mail: String): String
    fun deleteSpecificPill(id: String, mail: String)
    fun getLastPillId(mail: String): String?
}