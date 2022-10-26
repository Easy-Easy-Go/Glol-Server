package com.server.glol.domain.match.service.impl

import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.LeagueServiceFacade
import com.server.glol.domain.match.dto.*
import com.server.glol.domain.match.dto.projection.AllMatchVo
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.entities.Champion
import com.server.glol.domain.match.entities.Item
import com.server.glol.domain.match.entities.Match
import com.server.glol.domain.match.repository.*
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.match.service.MatchServiceFacade
import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.SummonerServiceFacade
import com.server.glol.global.config.banned.BannedAccountConfig
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MatchServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val summonerCustomRepository: SummonerCustomRepository,
    private val matchCustomRepository: MatchCustomRepository,
    private val matchRepository: MatchRepository,
    private val itemsRepository: ItemRepository,
    private val championRepository: ChampionRepository,
    private val summonerServiceFacade: SummonerServiceFacade,
    private val summonerService: SummonerService,
    private val matchServiceFacade: MatchServiceFacade,
    private val leagueService: LeagueService,
    private val leagueServiceFacade: LeagueServiceFacade
) : MatchService {

    @Transactional
    override fun renewalMatches(name: String, matchPageable: MatchPageable) {
        val puuid = getPuuid(name)

        val matchIds: MutableList<String> =
            matchServiceFacade.getMatchIds(puuid, matchPageable)

        val matches = getMatchesDetail(matchIds)

        matchesSave(matches)

        leagueService.saveLeague(name, leagueServiceFacade.getLeague(getId(name)))
    }

    override fun getMatch(matchId: String): MatchResponse {
        if (!matchRepository.existsByMatchId(matchId)) {
            return toMatchResponse(matchServiceFacade.getMatch(matchId))
        }

        return matchCustomRepository.findMatchesByMatchIds(matchId)!!
    }

    override fun getMatches(name: String, matchPageable: MatchPageable, pageable: Pageable): Page<AllMatchVo> {
        if (!summonerRepository.existsSummonerByName(name)) {
            throw IllegalArgumentException("Not found Summoner")
        }

        val matchIds: MutableList<String> = getMatchIds(name, matchPageable)

        return matchCustomRepository.findAllByMatchIds(name, matchIds, pageable)
    }

    private fun getMatchIds(name: String, renewalMatchesDto: MatchPageable): MutableList<String> {
        val matchIds = matchCustomRepository.findMatchIdBySummonerName(name)
        if (matchIds.isEmpty()) {
            return matchServiceFacade.getMatchIds(getPuuid(name), renewalMatchesDto)
        }
        return matchIds
    }

    private fun toMatchResponse(match: MatchDto): MatchResponse {
        val matchInfo: MutableList<MatchInfoTo> = mutableListOf()
        val metadata = MetadataTo(match.metadata.matchId, toQueueType(match.info.queueId), match.info.gameDuration)

        match.info.participants.forEach { participantDto ->

            matchInfo.add(
                MatchInfoTo(
                    participantDto.totalMinionsKilled,
                    participantDto.kills,
                    participantDto.assists,
                    participantDto.deaths,
                    participantDto.teamPosition,
                    participantDto.teamId,
                    participantDto.win,
                    participantDto.wardsPlaced,
                    participantDto.wardsKilled,
                    participantDto.wardsPlaced,
                    participantDto.totalDamageDealtToChampions,
                    participantDto.item0,
                    participantDto.item1,
                    participantDto.item2,
                    participantDto.item3,
                    participantDto.item4,
                    participantDto.item5,
                    participantDto.item6,
                    participantDto.championName,
                    participantDto.championId,
                    participantDto.champLevel,
                    participantDto.summonerName,
                    participantDto.summoner1Id,
                    participantDto.summoner2Id,
                )
            )

        }

        return MatchResponse(metadata, matchInfo)
    }

    private fun matchesSave(matches: MutableList<MatchDetailDto>) {

        matches.forEach Break@{ matchDetailDto ->
            val matchId: String = matchDetailDto.matchId

            if (matchRepository.countByMatchId(matchId) == 10L) {
                return@Break
            }
            val summoner = summonerRepository.findSummonerByName(matchDetailDto.name)
                ?: notExistsSummonerAccount(matchDetailDto.name)

            val match = matchRepository.save(
                Match(
                    matchId = matchId,
                    kills = matchDetailDto.kills,
                    assists = matchDetailDto.assists,
                    deaths = matchDetailDto.deaths,
                    teamPosition = matchDetailDto.teamPosition,
                    teamId = matchDetailDto.teamId,
                    win = matchDetailDto.win,
                    wardsPlaced = matchDetailDto.wardsPlaced,
                    wardsKilled = matchDetailDto.wardsKilled,
                    controlWardsPlaced = matchDetailDto.controlWardsPlaced,
                    createdAt = LocalDateTime.now(),
                    totalDamageDealtToChampions = matchDetailDto.totalDamageDealtToChampions,
                    totalMinionsKilled = matchDetailDto.totalMinionsKilled,
                    queueType = matchDetailDto.queueId,
                    gameDuration = matchDetailDto.gameDuration,
                    summoner = summoner,
                    firstSummonerSpell = matchDetailDto.summoner1Id,
                    secondSummonerSpell = matchDetailDto.summoner2Id
                )
            )

            itemsRepository.save(
                Item(
                    matchDetailDto.item0,
                    matchDetailDto.item1,
                    matchDetailDto.item2,
                    matchDetailDto.item3,
                    matchDetailDto.item4,
                    matchDetailDto.item5,
                    matchDetailDto.item6,
                    match
                )
            )

            championRepository.save(
                Champion(
                    matchDetailDto.championName,
                    matchDetailDto.championId,
                    matchDetailDto.championLevel,
                    match
                )
            )
        }
    }

    private fun notExistsSummonerAccount(summonerName: String): Summoner {
        summonerService.registerSummoner(summonerName)

        return summonerRepository.findSummonerByName(summonerName)
            ?: summonerRepository.findSummonerByName(BannedAccountConfig.name)!!
    }

    private fun getId(name: String): String {
        return summonerCustomRepository.findIdByName(name)
            ?: summonerServiceFacade.getSummoner(name).id
    }

    private fun getPuuid(name: String): String {
        return summonerCustomRepository.findPuuidByName(name)
            ?: summonerServiceFacade.getPuuid(name)
    }

    private fun getMatchesDetail(matchIdList: MutableList<String>): MutableList<MatchDetailDto> {
        val matchList: MutableList<MatchDto> = mutableListOf()

        matchIdList.forEach { matchId ->
            matchList.add(
                matchServiceFacade.getMatch(matchId)
            )
        }

        return toMatchDetailDto(matchList)
    }

    private fun toMatchDetailDto(matchDto: MutableList<MatchDto>): MutableList<MatchDetailDto> {
        val matchDetailDto: MutableList<MatchDetailDto> = mutableListOf()

        matchDto.forEach { matchDto ->
            val matchId = matchDto.metadata.matchId
            val queueId = toQueueType(matchDto.info.queueId)
            val gameDuration = matchDto.info.gameDuration

            matchDto.info.participants.forEach { participant ->
                matchDetailDto.add(
                    MatchDetailDto(
                        matchId = matchId,
                        kills = participant.kills,
                        assists = participant.assists,
                        deaths = participant.deaths,
                        teamPosition = participant.teamPosition,
                        teamId = participant.teamId,
                        win = participant.win,
                        wardsPlaced = participant.wardsPlaced,
                        wardsKilled = participant.wardsKilled,
                        controlWardsPlaced = participant.challenges.controlWardsPlaced,
                        item0 = participant.item0,
                        item1 = participant.item1,
                        item2 = participant.item2,
                        item3 = participant.item3,
                        item4 = participant.item4,
                        item5 = participant.item5,
                        item6 = participant.item6,
                        championName = participant.championName,
                        championId = participant.championId,
                        championLevel = participant.champLevel,
                        name = participant.summonerName,
                        queueId = queueId,
                        gameDuration = gameDuration,
                        totalMinionsKilled = participant.totalMinionsKilled,
                        totalDamageDealtToChampions = participant.totalDamageDealtToChampions,
                        summoner1Id = participant.summoner1Id,
                        summoner2Id = participant.summoner2Id,
                    )
                )
            }
        }
        return matchDetailDto
    }

    private fun toQueueType(queueId: Int): String = when (queueId) {
        420 -> "솔랭"
        430 -> "일반"
        440 -> "자유랭크"
        450 -> "무작위 총력전"
        700 -> "격전"
        920 -> "포로왕"
        1020 -> "단일"
        1400 -> "궁극기 주문서"
        1900 -> "우르프"
        else -> {
            "기타"
        }
    }
}