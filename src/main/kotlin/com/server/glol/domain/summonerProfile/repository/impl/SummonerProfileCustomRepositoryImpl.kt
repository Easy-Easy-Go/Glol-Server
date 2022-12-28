package com.server.glol.domain.summonerProfile.repository.impl

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.repository.SummonerProfileCustomRepository
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import com.server.glol.domain.summonerProfile.dto.projection.QSummonerProfileResponse
import com.server.glol.domain.summonerProfile.entities.QSummonerProfile.summonerProfile
import com.server.glol.domain.summonerProfile.entities.SummonerProfile
import org.springframework.stereotype.Repository

@Repository
class SummonerProfileCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : SummonerProfileCustomRepository {
    override fun getSummonerProfileByName(name: String): MutableSet<SummonerProfileResponse> =
        query.select(
            QSummonerProfileResponse(
                summonerProfile.queueType,
                summonerProfile.rank,
                summonerProfile.tier,
                Expressions.constantAs(name, summonerProfile.summoner.name),
                summonerProfile.leaguePoints,
                summonerProfile.wins,
                summonerProfile.losses,
                summonerProfile.winRate,
                summonerProfile.games
            )
        )
            .where(summonerProfile.summoner.name.eq(name))
            .from(summonerProfile)
            .fetch().toMutableSet()

    override fun getSummonerProfileByQueueTypeAndName(name: String, queueType: String) =
        query.selectFrom(summonerProfile)
            .innerJoin(summonerProfile.summoner, summoner)
            .where(summoner.name.eq(name), summonerProfile.queueType.eq(queueType))
            .fetchOne()

    override fun getHighRankSummonerProfileByName(name: String): SummonerProfile = query.selectFrom(summonerProfile)
        .innerJoin(summonerProfile.summoner, summoner)
        .where(summoner.name.eq(name))
        .orderBy(summonerProfile.rankScore.desc())
        .fetchFirst()
}