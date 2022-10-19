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

        match.info.participants.forEach {
            it.perks.styles.stream().map { it.selections.stream().map { print("==================== Perk is = $it.perk =================")
                perks.add(it.perk) } }
            matchInfo.add(
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
                    it.wardsPlaced,
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
                    it.champLevel,
                    it.summonerName,
                    it.summoner1Id,
                    it.summoner2Id,
                    perks
                )
            )

        }

        return MatchResponse(metadata, matchInfo)
    }

    private fun matchesSave(matches: MutableList<MatchDetailDto>) {
        var match: Match
        var matchId: String

        matches.forEach Break@{
            matchId = it.matchId

            if (matchRepository.countByMatchId(matchId) == 10L) {
                return@Break
            }
            val summoner = summonerRepository.findSummonerByName(it.name)
                ?: ifNotExistsSummonerAccount(it.name)

            match = matchRepository.save(
                Match(
                    matchId = matchId,
                    kills = it.kills,
                    assists = it.assists,
                    deaths = it.deaths,
                    teamPosition = it.teamPosition,
                    teamId = it.teamId,
                    win = it.win,
                    wardsPlaced = it.wardsPlaced,
                    wardsKilled = it.wardsKilled,
                    controlWardsPlaced = it.controlWardsPlaced,
                    createdAt = LocalDateTime.now(),
                    totalDamageDealtToChampions = it.totalDamageDealtToChampions,
                    totalMinionsKilled = it.totalMinionsKilled,
                    queueType = it.queueId,
                    gameDuration = it.gameDuration,
                    summoner = summoner,
                    firstSummonerSpell = it.summoner1Id,
                    secondSummonerSpell = it.summoner2Id
                )
            )

            itemsRepository.save(Item(it.item0, it.item1, it.item2, it.item3, it.item4, it.item5, it.item6, match))

            championRepository.save(Champion(it.championName, it.championId, it.championLevel, match))

            it.perks.styles.forEach {
                it.selections.forEach {
                    perkRepository.save(Perk(it.perk, match))
                }
            }
        }
    }

    private fun ifNotExistsSummonerAccount(summonerName: String): Summoner {
        val summoner = summonerService.registrationSummoner(summonerName)
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
            .uri(riotProperties.matchUUIDUrl + puuid + "/ids?queue=" + queue + "&count=" + count).headers {
                it.contentType = MediaType.APPLICATION_JSON
                it.acceptCharset = listOf(StandardCharsets.UTF_8)
                it.set("X-Riot-Token", riotProperties.secretKey)
                it.set("Origin", riotProperties.origin)
            }.retrieve().bodyToMono(string::class.java).block()
            ?: throw IllegalArgumentException("Not Exists Matches")
    }

    private fun getMatchesByMatchId(matchIdList: MutableList<String>): MutableList<MatchDetailDto> {
        val matchList: MutableList<MatchDto> = mutableListOf()

        matchIdList.forEach {
            matchList.add(
                getMatchByMatchId(it)
            )
        }

        return toMatchDetailDto(matchList)
    }

    private fun getMatchByMatchId(matchId: String): MatchDto {
        return WebClient.create().get().uri(riotProperties.matchesMatchIdUrl + matchId).headers {
            it.contentType = MediaType.APPLICATION_JSON
            it.acceptCharset = listOf(StandardCharsets.UTF_8)
            it.set("X-Riot-Token", riotProperties.secretKey)
            it.set("Origin", riotProperties.origin)
        }.retrieve().bodyToMono(MatchDto().javaClass).block()
            ?: throw IllegalArgumentException("Not Exists Match")
    }

    private fun toMatchDetailDto(matchDto: MutableList<MatchDto>): MutableList<MatchDetailDto> {
        val matchDetailDto: MutableList<MatchDetailDto> = mutableListOf()

        matchDto.forEach {
            val matchId = it.metadata.matchId
            val queueId = toQueueType(it.info.queueId)
            val gameDuration = it.info.gameDuration

            it.info.participants.forEach {
                matchDetailDto.add(
                    MatchDetailDto(
                        matchId = matchId,
                        kills = it.kills,
                        assists = it.assists,
                        deaths = it.deaths,
                        teamPosition = it.teamPosition,
                        teamId = it.teamId,
                        win = it.win,
                        wardsPlaced = it.wardsPlaced,
                        wardsKilled = it.wardsKilled,
                        controlWardsPlaced = it.challenges.controlWardsPlaced,
                        item0 = it.item0,
                        item1 = it.item1,
                        item2 = it.item2,
                        item3 = it.item3,
                        item4 = it.item4,
                        item5 = it.item5,
                        item6 = it.item6,
                        championName = it.championName,
                        championId = it.championId,
                        championLevel = it.champLevel,
                        name = it.summonerName,
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