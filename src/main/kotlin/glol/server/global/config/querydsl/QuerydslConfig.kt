package com.server.glol.global.config.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import javax.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired

@Configuration
class QuerydslConfig(@Autowired val em: EntityManager) {

    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(em)
}