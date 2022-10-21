package com.server.glol.domain.match.service.impl

import com.server.glol.domain.match.dto.MatchDetailDto
import com.server.glol.domain.match.dto.MatchInfoTo
import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.MetadataTo
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.entities.Champion
import com.server.glol.domain.match.entities.Item
import com.server.glol.domain.match.entities.Match
import com.server.glol.domain.match.entities.Perk
import com.server.glol.domain.match.repository.*
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Service
class MatchServiceImpl(
    val riotProperties: RiotProperties,
    val summonerRepository: SummonerRepository,
    val summonerCustomRepository: SummonerCustomRepository,
    val matchCustomRepository: MatchCustomRepository,
    val matchRepository: MatchRepository,
    val itemsRepository: ItemRepository,
    val championRepository: ChampionRepository,
    val summonerService: SummonerService,
    val perkRepository: PerkRepository,
) : MatchService {

    @Transactional
    override fun renewalMatches(name: String, queue: Int, count: Int) {
        val puuid = getPuuidByName(name)

        val matchIds: MutableList<String> = getMatchIdsByPuuid(puuid, queue, count)

        val matches = getMatchesByMatchId(matchIds)

        matchesSave(matches)
    }

    override fun getMatch(matchId: String): MatchResponse {
        if (!matchRepository.existsByMatchId(matchId)) {
            return toMatchResponse(getMatchByMatchId(matchId))
        }

        return matchCustomRepository.findMatchesByMatchIds(matchId)!!
    }

    private fun toMatchResponse(match: MatchDto): MatchResponse {
        val matchInfo: MutableList<MatchInfoTo> = mutableListOf()
        val metadata = MetadataTo(match.metadata.matchId, toQueueType(match.info.queueId), match.info.gameDuration)
        val perks: MutableList<Int> = mutableListOf()

        match.info.participants.forEach { participantDto ->
            participantDto.perks.styles.stream().map { perkStyleDto ->
                perkStyleDto.selections.stream().map { perkStyleSelectionDto ->
                    perks.add(perkStyleSelectionDto.perk)
                }
            }
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
                    perks
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

            matchDetailDto.perks.styles.forEach { perkStyleDto ->
                perkStyleDto.selections.forEach { perkStyleSelectionDto ->
                    perkRepository.save(Perk(perkStyleSelectionDto.perk, match))
                }
            }
        }
    }

    private fun ifNotExistsSummonerAccount(summonerName: String): Summoner {
        val summoner = summonerService.registerSummoner(summonerName)
        return summonerRepository.findSummonerByName(summoner.name)
            ?: throw IllegalArgumentException("retry please")
    }

    private fun getPuuidByName(name: String): String {
        return summonerCustomRepository.findPuuidByName(name)
            ?: summonerService.getPuuidByName(name)
    }

    private fun getMatchIdsByPuuid(puuid: String, queue: Int, count: Int): MutableList<String> {
        val string: MutableList<String> = mutableListOf()

        return WebClient.create().get()
            .uri(riotProperties.matchUUIDUrl + puuid + "/ids?queue=" + queue + "&count=" + count)
            .headers { httpHeaders ->
                httpHeaders.contentType = MediaType.APPLICATION_JSON
                httpHeaders.acceptCharset = listOf(StandardCharsets.UTF_8)
                httpHeaders.set("X-Riot-Token", riotProperties.secretKey)
                httpHeaders.set("Origin", riotProperties.origin)
            }.retrieve().bodyToMono(string::class.java).block()
            ?: throw IllegalArgumentException("Not Exists Matches")
    }

    private fun getMatchesByMatchId(matchIdList: MutableList<String>): MutableList<MatchDetailDto> {
        val matchList: MutableList<MatchDto> = mutableListOf()

        matchIdList.forEach { matchId ->
            matchList.add(
                getMatchByMatchId(matchId)
            )
        }

        return toMatchDetailDto(matchList)
    }

    private fun getMatchByMatchId(matchId: String): MatchDto {
        return WebClient.create().get().uri(riotProperties.matchesMatchIdUrl + matchId).headers { httpHeaders ->
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            httpHeaders.acceptCharset = listOf(StandardCharsets.UTF_8)
            httpHeaders.set("X-Riot-Token", riotProperties.secretKey)
            httpHeaders.set("Origin", riotProperties.origin)
        }.retrieve().bodyToMono(MatchDto().javaClass).block()
            ?: throw IllegalArgumentException("Not Exists Match")
    }

    private fun toMatchDetailDto(matchDto: MutableList<MatchDto>): MutableList<MatchDetailDto> {
        val matchDetailDto: MutableList<MatchDetailDto> = mutableListOf()

        matchDto.forEach { matchDto ->
            val matchId = matchDto.metadata.matchId
            val queueId = toQueueType(matchDto.info.queueId)
            val gameDuration = matchDto.info.gameDuration

            matchDto.info.participants.forEach { participantDto ->
                matchDetailDto.add(
                    MatchDetailDto(
                        matchId = matchId,
                        kills = participantDto.kills,
                        assists = participantDto.assists,
                        deaths = participantDto.deaths,
                        teamPosition = participantDto.teamPosition,
                        teamId = participantDto.teamId,
                        win = participantDto.win,
                        wardsPlaced = participantDto.wardsPlaced,
                        wardsKilled = participantDto.wardsKilled,
                        controlWardsPlaced = participantDto.challenges.controlWardsPlaced,
                        item0 = participantDto.item0,
                        item1 = participantDto.item1,
                        item2 = participantDto.item2,
                        item3 = participantDto.item3,
                        item4 = participantDto.item4,
                        item5 = participantDto.item5,
                        item6 = participantDto.item6,
                        championName = participantDto.championName,
                        championId = participantDto.championId,
                        championLevel = participantDto.champLevel,
                        name = participantDto.summonerName,
                        queueId = queueId,
                        gameDuration = gameDuration,
                        totalMinionsKilled = it.totalMinionsKilled,
                        totalDamageDealtToChampions = it.totalDamageDealtToChampions,
                        summoner1Id = it.summoner1Id,
                        summoner2Id = it.summoner2Id,
                        perks = it.perks
                    )
                )
            }
        }
        return matchDetailDto
    }

    private fun toQueueType(queueId: Int): String {
        return when (queueId) {
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
}