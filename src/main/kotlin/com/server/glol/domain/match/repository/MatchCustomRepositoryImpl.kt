package com.server.glol.domain.match.repository

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.match.entities.QChampion.champion
import com.server.glol.domain.match.entities.QItem.item
import com.server.glol.domain.match.entities.QMatch.match
import com.server.glol.domain.match.repository.projection.MatchResponse
import com.server.glol.domain.match.repository.projection.QMatchVo
import com.server.glol.domain.summoner.entites.QSummoner.summoner
import org.springframework.stereotype.Repository

@Repository
class MatchCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : MatchCustomRepository {

    override fun findMatchIdBySummonerName(name: String): MutableList<String> {
        return query.select(match.matchId)
            .from(match)
            .innerJoin(match.summoner, summoner)
            .where(summoner.name.eq(name))
            .fetch()
    }

    override fun findMatchesByMatchIds(matchIds: MutableList<String>): MutableList<MatchResponse>? {
        val matches: MutableList<MatchResponse> = mutableListOf()

        matchIds.forEach {
            matches.add(
                MatchResponse(
                    query.select(
                        QMatchVo(
                            Expressions.constantAs(it, match.matchId),
                            match.kills,
                            match.assists,
                            match.deaths,
                            match.teamPosition,
                            match.teamId,
                            match.win,
                            match.wardsPlaced,
                            match.wardsKilled,
                            match.controlWardsPlaced,
                            match.item.item0,
                            match.item.item1,
                            match.item.item2,
                            match.item.item3,
                            match.item.item4,
                            match.item.item5,
                            match.item.item6,
                            match.champion.championName,
                            match.champion.championId,
                            match.champion.championLevel,
                            match.summoner.name
                        )
                    ).distinct()
                        .from(match)
                        .innerJoin(match.summoner, summoner)
                        .innerJoin(match.item, item)
                        .innerJoin(match.champion, champion)
                        .where(match.matchId.eq(it))
                        .fetch()
                )
            )
        }

        return matches
    }

}