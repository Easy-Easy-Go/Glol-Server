package com.server.glol.domain.match.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.match.dto.MatchInfoTo
import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.MetadataTo
import com.server.glol.domain.match.dto.projection.MatchDao
import com.server.glol.domain.match.dto.projection.MetadataDao
import com.server.glol.domain.match.dto.projection.QMatchDao
import com.server.glol.domain.match.dto.projection.QMetadataDao
import com.server.glol.domain.match.entities.QChampion.champion
import com.server.glol.domain.match.entities.QItem.item
import com.server.glol.domain.match.entities.QMatch.match
import com.server.glol.domain.match.entities.QPerk.perk
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

        val runes: MutableList<Int> = query.select(perk.rune)
            .from(perk)
            .where(perk.match.matchId.eq(matchId))
            .fetch()

        val matchDao: MutableList<MatchDao>? = query.select(
            QMatchDao(
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
            .innerJoin(match._perks, perk)
            .where(match.matchId.eq(matchId))
            .fetch()

        val metadataTo: MetadataDao = query.select(
            QMetadataDao(
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

        var first = 0
        var second = 6
        matchDao!!.forEach {
            matchInfoDto.add(
                MatchInfoTo(
                    it.totalMinionsKilled,
                    it.kills,
                    it.assists,
                    it.deaths,
                    it.teamPosition,
                    it.teamId,
                    it.win,
                    it.wardsPlaced,
                    it.wardsKilled,
                    it.controlWardsPlaced,
                    it.totalDamageDealtToChampions,
                    it.item0,
                    it.item1,
                    it.item2,
                    it.item3,
                    it.item4,
                    it.item5,
                    it.item6,
                    it.championName,
                    it.championId,
                    it.championLevel,
                    it.name,
                    it.firstSummonerSpell,
                    it.secondSummonerSpell,
                    runes.subList(first, second)
                )
            )
            first += 6
            second += 6
        }

        return MatchResponse(MetadataTo(metadataTo.matchId, metadataTo.queueId, metadataTo.gameDuration), matchInfoDto)
    }

}