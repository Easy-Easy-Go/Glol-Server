package com.server.glol.domain.summoner.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.summoner.entites.QSummoner.summoner
import com.server.glol.domain.summoner.entites.Summoner
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

    override fun findPuuidByName(name: String): Summoner? {
        return query.selectFrom(
            summoner)
                .where(summoner.name.eq(name))
                .fetchOne()
    }
}