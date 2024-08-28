package com.yucox.pillpulseapiv1.repository

import com.yucox.pillpulseapiv1.model.Pill
import com.yucox.pillpulseapiv1.model.PillRepositoryCustom
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.count
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class PillRepositoryCustomImpl(private val mongoTemplate: MongoTemplate) : PillRepositoryCustom {
    override fun getPaginated(
        mail: String,
        requestedMonth: Int,
        page: Int,
        limit: Int
    ): List<Pill> {
        val criteria = Criteria.where("userMail").`is`(mail)
            .and("month").`is`(requestedMonth.toString())
        val query = Query()
            .with(Sort.by(Sort.Direction.DESC, "id"))
        query.addCriteria(criteria).skip(page.toLong()).limit(limit)
        return mongoTemplate.find(query, Pill::class.java)
    }

    override fun get(mail: String): List<Pill> {
        val criteria = Criteria.where("userMail").`is`(mail)
        val query = Query(criteria)
            .with(Sort.by(Sort.Direction.DESC, "id"))
        return mongoTemplate.find(query, Pill::class.java)
    }

    override fun getItemCount(mail: String): String {
        val criteria = Criteria.where("userMail").`is`(mail)
        val query = Query(criteria)
        return mongoTemplate.count(query, Pill::class.java).toString()
    }

    override fun deleteSpecificPill(id: String, mail: String) {
        val criteria = Criteria.where("userMail").`is`(mail)
            .and("id").`is`(id)
        val query = Query(criteria)
        mongoTemplate.remove(query, Pill::class.java)
    }

    override fun getLastPillId(mail: String): String? {
        val criteria = Criteria.where("userMail").`is`(mail)
        val query = Query().addCriteria(criteria)
            .with(Sort.by(Sort.Order.desc("id")))
            .limit(1)
        return mongoTemplate.findOne(query, Pill::class.java)?.id
    }
}