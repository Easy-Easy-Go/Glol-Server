package com.server.glol.domain.summoner.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import com.server.glol.domain.summoner.repository.projection.QSummonerVo
import com.server.glol.domain.summoner.repository.projection.SummonerVo
import org.springframework.stereotype.Repository

@Repository
class SummonerCustomRepositoryImpl(private val query: JPAQueryFactory) : SummonerCustomRepository {
    override fun findSummonerByName(name: String): SummonerVo? {
        return query.select(
            QSummonerVo(
                summoner.id,
                summoner.accountId,
                summoner.name,
                summoner.puuid,
                summoner.profileIconId,
                summoner.visited
            )
        )
            .from(summoner)
            .where(summoner.name.eq(name))
            .fetchOne()
    }

    override fun findPuuidByName(name: String): String?
        = query.select(summoner.puuid)
            .where(summoner.name.eq(name))
            .from(summoner)
            .fetchOne()

    override fun findIdByName(name: String): String?
        = query.select(summoner.id)
            .where(summoner.name.eq(name))
            .from(summoner)
            .fetchOne()
}