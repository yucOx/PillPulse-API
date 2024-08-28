package com.yucox.pillpulseapiv1.service

import com.yucox.pillpulseapiv1.model.Pill
import com.yucox.pillpulseapiv1.repository.PillRepository
import com.yucox.pillpulseapiv1.repository.PillRepositoryCustomImpl
import org.springframework.stereotype.Service

@Service
class PillService(
    private val pillRepository: PillRepository,
    private val pillRepositoryCustomImpl: PillRepositoryCustomImpl
) {
    fun getPaginatedPillList(
        mail: String,
        requestedMonth: Int,
        page: Int,
        limit: Int
    ): List<Pill>? {
        return pillRepositoryCustomImpl.getPaginated(mail, requestedMonth, page, limit)
    }

    fun getAllPills(mail: String): List<Pill>? {
        return pillRepositoryCustomImpl.get(mail)
    }

    fun deletePill(id: String, mail: String) {
        pillRepositoryCustomImpl.deleteSpecificPill(id, mail)
    }

    fun savePill(mail: String, pill: Pill) {
        val newId = pillRepositoryCustomImpl.getLastPillId(mail)

        val pillWithId = pill.apply {
            this.userMail = mail
            this.id = if (newId.isNullOrBlank())
                "1"
            else
                (newId.toInt() + 1).toString()
        }
        pillRepository.save(pillWithId)
    }

}