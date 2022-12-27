package com.server.glol.domain.summonerProfile.repository.impl

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.dto.projection.QLeagueResponse
import com.server.glol.domain.summonerProfile.entities.QLeague.league
import com.server.glol.domain.summonerProfile.repository.SummonerProfileCustomRepository
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import org.springframework.stereotype.Repository

@Repository
class SummonerProfileCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : SummonerProfileCustomRepository {
    override fun getSummonerProfileByName(name: String): MutableSet<SummonerProfileResponse> =
        query.select(
            QLeagueResponse(
                league.queueType,
                league.rank,
                Expressions.constantAs(name, league.summoner.name),
                league.leaguePoints,
                league.wins,
                league.losses,
                league.winRate,
                league.games
            )
        )
            .where(league.summoner.name.eq(name))
            .from(league)
            .fetch().toMutableSet()

    override fun getSummonerProfile(name: String, queueType: String) =
        query.selectFrom(league)
            .innerJoin(league.summoner, summoner)
            .where(summoner.name.eq(name), league.queueType.eq(queueType))
            .fetchOne()
}