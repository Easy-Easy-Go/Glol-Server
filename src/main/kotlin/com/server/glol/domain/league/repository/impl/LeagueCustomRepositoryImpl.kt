package com.server.glol.domain.league.repository.impl

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.league.dto.projection.LeagueResponse
import com.server.glol.domain.league.dto.projection.QLeagueResponse
import com.server.glol.domain.league.entities.QLeague.league
import com.server.glol.domain.league.repository.LeagueCustomRepository
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import org.springframework.stereotype.Repository

@Repository
class LeagueCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : LeagueCustomRepository {
    override fun getLeagueEntryByName(name: String): MutableSet<LeagueResponse> =
        query.select(
            QLeagueResponse(
                league.queueType,
                league.tier,
                league.rank,
                Expressions.constantAs(name, league.summoner.name),
                league.leaguePoints,
                league.wins,
                league.losses
            )
        )
            .where(league.summoner.name.eq(name))
            .from(league)
            .fetch().toMutableSet()

    override fun getLeague(name: String, queueType: String) =
        query.selectFrom(league)
            .innerJoin(league.summoner, summoner)
            .where(summoner.name.eq(name), league.queueType.eq(queueType))
            .fetchOne()
}