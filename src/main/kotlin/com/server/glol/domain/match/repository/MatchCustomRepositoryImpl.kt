package com.server.glol.domain.match.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.match.dto.*
import com.server.glol.domain.match.dto.projection.*
import com.server.glol.domain.match.entities.QChampion.champion
import com.server.glol.domain.match.entities.QItem.item
import com.server.glol.domain.match.entities.QMatch.match
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

    override fun findMatchesByMatchIds(matchId: String): MatchResponse? {

        val matchDao: MutableList<MatchVo>? = query.select(
            QMatchVo(
                match.totalMinionsKilled,
                match.kills,
                match.assists,
                match.deaths,
                match.teamPosition,
                match.teamId,
                match.win,
                match.wardsPlaced,
                match.wardsKilled,
                match.controlWardsPlaced,
                match.totalDamageDealtToChampions,
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
                match.summoner.name,
                match.firstSummonerSpell,
                match.secondSummonerSpell,
            )
        )
            .distinct()
            .from(match)
            .innerJoin(match.summoner, summoner)
            .innerJoin(match.item, item)
            .innerJoin(match.champion, champion)
            .where(match.matchId.eq(matchId))
            .fetch()

        val metadataTo: MetadataVo = query.select(
            QMetadataVo(
                match.matchId,
                match.queueType,
                match.gameDuration
            )
        )
            .distinct()
            .from(match)
            .where(match.matchId.eq(matchId))
            .fetchOne()!!

        val matchInfoDto: MutableList<MatchInfoTo> = mutableListOf()

        matchDao!!.forEachIndexed { index, match ->
            matchInfoDto.add(
                MatchInfoTo(
                    match.totalMinionsKilled,
                    match.kills,
                    match.assists,
                    match.deaths,
                    match.teamPosition,
                    match.teamId,
                    match.win,
                    match.wardsPlaced,
                    match.wardsKilled,
                    match.controlWardsPlaced,
                    match.totalDamageDealtToChampions,
                    match.item0,
                    match.item1,
                    match.item2,
                    match.item3,
                    match.item4,
                    match.item5,
                    match.item6,
                    match.championName,
                    match.championId,
                    match.championLevel,
                    match.name,
                    match.firstSummonerSpell,
                    match.secondSummonerSpell,
                )
            )
        }

        return MatchResponse(MetadataTo(metadataTo.matchId, metadataTo.queueId, metadataTo.gameDuration), matchInfoDto)
    }

}