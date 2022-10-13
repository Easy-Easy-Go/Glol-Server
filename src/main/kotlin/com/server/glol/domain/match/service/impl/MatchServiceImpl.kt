package com.server.glol.domain.match.service.impl

import com.server.glol.domain.match.dto.MatchDto
import com.server.glol.domain.match.entities.Champion
import com.server.glol.domain.match.entities.Item
import com.server.glol.domain.match.entities.Match
import com.server.glol.domain.match.repository.ChampionRepository
import com.server.glol.domain.match.repository.ItemRepository
import com.server.glol.domain.match.repository.MatchCustomRepository
import com.server.glol.domain.match.repository.MatchRepository
import com.server.glol.domain.match.repository.projection.MatchResponse
import com.server.glol.domain.match.repository.projection.MatchVo
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
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
) : MatchService {

    override fun renewalMatches(name: String, queue: Int, count: Int) {
        val puuid = getPuuidByName(name)

        val matchIds: MutableList<String> = getMatchIdsByPuuid(puuid, queue, count)

        val matches = getMatchesByMatchId(matchIds)

        matchesSave(matches)
    }

    override fun getMatches(name: String, queue: Int, count: Int): MutableList<MatchResponse>? {
        val matchIds: MutableList<String> = getRecentMatch(name)

        if (!summonerRepository.existsSummonerByName(name)) {
            val summoner = summonerService.getSummoner(name)

            return getMatchesByMatchId(getMatchIdsByPuuid(summoner.puuid, queue, count))
        }

        return matchCustomRepository.findMatchesByMatchIds(matchIds)
    }

    private fun matchesSave(matches: MutableList<MatchResponse>) {
        var match: Match
        var matchId: String


        matches.forEach {
            it.matchesVo.forEach Break@{
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
                        renewedAt = null,
                        summoner = summoner
                    )
                )

                itemsRepository.saveAll(
                    mutableListOf(
                        Item(
                            it.item0, it.item1, it.item2, it.item3, it.item4, it.item5, it.item6, match
                        )
                    )
                )

                championRepository.saveAll(
                    mutableListOf(
                        Champion(it.championName, it.championId, it.championLevel, match)
                    )
                )
            }
        }
    }

    private fun ifNotExistsSummonerAccount(summonerName: String): Summoner {
        val summoner = summonerService.registrationSummoner(summonerName)
        return summonerRepository.findSummonerByName(summoner.name)
            ?: throw IllegalArgumentException("retry please")
    }

    private fun getRecentMatch(name: String): MutableList<String> {
        return matchCustomRepository.findMatchIdBySummonerName(name)
    }

    private fun getPuuidByName(name: String): String {
        return summonerCustomRepository.findPuuidByName(name)?:
        summonerService.getSummoner(name).puuid
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

    private fun getMatchesByMatchId(matchIdList: MutableList<String>): MutableList<MatchResponse> {
        val matchList: MutableList<MatchDto> = mutableListOf()

        matchIdList.forEach {
            matchList.add(
                WebClient.create().get().uri(riotProperties.matchesMatchIdUrl + it).headers {
                    it.contentType = MediaType.APPLICATION_JSON
                    it.acceptCharset = listOf(StandardCharsets.UTF_8)
                    it.set("X-Riot-Token", riotProperties.secretKey)
                    it.set("Origin", riotProperties.origin)
                }.retrieve().bodyToMono(MatchDto().javaClass).block()
                    ?: throw IllegalArgumentException("Not Exists Match")
            )
        }

        return toMatchResponse(matchList)
    }

    private fun toMatchResponse(matchDto: MutableList<MatchDto>): MutableList<MatchResponse> {
        val matchesVo: MutableList<MatchVo> = mutableListOf()
        val matchesResponse: MutableList<MatchResponse> = mutableListOf()
        var matchId: String

        matchDto.forEach {
            matchId = it.metadata.matchId
            it.info.participants.forEach {
                matchesVo.add(
                    MatchVo(
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
                        name = it.summonerName
                    )
                )
            }
        }
        matchesResponse.add(MatchResponse(matchesVo))

        return matchesResponse
    }
}