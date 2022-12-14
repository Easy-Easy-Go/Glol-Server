package com.server.glol.domain.match.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.champion.entities.QChampion.champion
import com.server.glol.domain.item.entities.QItem.item
import com.server.glol.domain.match.dto.MatchInfoDto
import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.MetadataDto
import com.server.glol.domain.match.dto.projection.*
import com.server.glol.domain.match.entities.QMatch.match
import com.server.glol.domain.match.repository.MatchCustomRepository
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MatchCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : MatchCustomRepository {

    override fun findMatchIdBySummonerName(name: String): MutableList<String> = query.select(match.matchId)
        .from(match)
        .innerJoin(match.summoner, summoner)
        .where(summoner.name.eq(name))
        .fetch()

    override fun findMatchesByMatchIds(matchId: String): MatchResponse? {

        val matchDao: MutableList<MatchDto>? = query.select(
            QMatchDto(
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

        val metadataTo: com.server.glol.domain.match.dto.projection.MetadataDto = query.select(
            QMetadataDto(
                match.matchId,
                match.queueType,
                match.gameDuration
            )
        )
            .distinct()
            .from(match)
            .where(match.matchId.eq(matchId))
            .fetchOne()!!

        val matchInfoDto: MutableList<MatchInfoDto> = mutableListOf()

        matchDao!!.forEach { match ->
            matchInfoDto.add(
                MatchInfoDto(
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

        return MatchResponse(MetadataDto(metadataTo.matchId, metadataTo.queueId, metadataTo.gameDuration), matchInfoDto)
    }

    override fun findAllByMatchIds(name: String, matchIds: MutableList<String>, pageable: Pageable): Page<MatchesDto> {

        val matches: MutableList<MatchesDto> = query.select(
            QMatchesDto(
                match.matchId,
                match.gameDuration,
                match.queueType,
                match.totalMinionsKilled,
                match.kills,
                match.assists,
                match.deaths,
                match.win,
                match.wardsPlaced,
                match.wardsKilled,
                match.controlWardsPlaced,
                match.firstSummonerSpell,
                match.secondSummonerSpell,
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
            )
        )
            .from(match)
            .where(match.matchId.`in`(matchIds), summoner.name.eq(name))
            .innerJoin(match.summoner, summoner)
            .innerJoin(match.item, item)
            .innerJoin(match.champion, champion)
            .orderBy(match.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: Long? = query.select(match.count())
            .from(match)
            .where(match.matchId.`in`(matchIds))
            .fetchOne()

        return PageImpl(matches, pageable, count!!)
    }

    override fun findLastMatchIdBySummonerName(name: String): String? = query.select(match.matchId)
        .from(match)
        .innerJoin(match.summoner, summoner)
        .where(summoner.name.eq(name))
        .orderBy(match.gameCreation.desc())
        .fetchFirst()
}