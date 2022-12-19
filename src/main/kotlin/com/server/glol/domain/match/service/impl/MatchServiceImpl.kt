package com.server.glol.domain.match.service.impl

import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.match.dto.*
import com.server.glol.domain.match.dto.projection.MatchesDto
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.entities.Champion
import com.server.glol.domain.match.entities.Item
import com.server.glol.domain.match.entities.Match
import com.server.glol.domain.match.repository.ChampionRepository
import com.server.glol.domain.match.repository.ItemRepository
import com.server.glol.domain.match.repository.MatchCustomRepository
import com.server.glol.domain.match.repository.MatchRepository
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.match.service.facade.RemoteMatchFacade
import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.facade.RemoteSummonerFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val summonerService: SummonerService,
    private val matchCustomRepository: MatchCustomRepository,
    private val matchRepository: MatchRepository,
    private val itemRepository: ItemRepository,
    private val remoteMatchFacade: RemoteMatchFacade,
    private val championRepository: ChampionRepository,
    private val leagueService: LeagueService,
    private val remoteSummonerFacade: RemoteSummonerFacade,
) : MatchService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    override fun renewalMatches(name: String, matchPageable: MatchPageable) {

        isNotExistsSummonerCheck(name)

        val puuid = summonerService.getPuuid(name)

        remoteMatchFacade.getMatchIds(puuid, matchPageable)
            .filterNot { matchRepository.existsByMatchId(it) }.toMutableList()
            .let { matchIds -> entitySave(getMatchesDetail(matchIds)) }

        leagueService.saveLeague(name)
    }

    override fun getMatch(matchId: String): MatchResponse {
        if (isNotExistsMatch(matchId)) {
            return toMatchResponse(remoteMatchFacade.getMatch(matchId))
        }

        return matchCustomRepository.findMatchesByMatchIds(matchId)!!
    }

    override fun getMatches(name: String, matchPageable: MatchPageable, pageable: Pageable): Page<MatchesDto> {
        if (isNotExistsSummoner(name)) {
            throw CustomException(NOT_FOUND_SUMMONER)
        }

        return getMatchIds(name, matchPageable).let { matchIds ->
            matchCustomRepository.findAllByMatchIds(name, matchIds, pageable)
        }
    }

    private fun getMatchIds(name: String, renewalMatchesDto: MatchPageable): MutableList<String> {
        val matchIds = matchCustomRepository.findMatchIdBySummonerName(name)

        if (matchIds.isEmpty()) {
            return remoteMatchFacade.getMatchIds(summonerService.getPuuid(name), renewalMatchesDto)
        }
        return matchIds
    }

    private fun toMatchResponse(match: MatchDto): MatchResponse {
        return match.toMetadataDto()
            .let { metadata ->
                match.info.participants.map { MatchInfoDto(it) }.toMutableList()
                    .let { matchInfo -> MatchResponse(metadata, matchInfo) }
            }
    }

    private fun MatchDto.toMetadataDto(): MetadataDto {
        return MetadataDto(
            matchId = this.metadata.matchId,
            queueId = toQueueType(this.info.queueId),
            gameDuration = this.info.gameDuration
        )
    }

    private fun entitySave(matches: MutableList<MatchDetailDto>) {
        matches.forEach { matchDetail ->
            participantsSave(matchDetail)

            summonerRepository.findSummonerByName(matchDetail.name)!!
                .let { summoner ->
                    matchRepository.save(Match(matchDetail, summoner))
                }.let { match ->
                    itemRepository.save(Item(matchDetail, match))
                    championRepository.save(Champion(matchDetail, match))
                }
        }
    }

    private fun participantsSave(match: MatchDetailDto) {
        runBlocking {
            match.participantsPuuid.filterNot { puuid ->
                summonerRepository.existsSummonerByPuuid(puuid)
            }.map { puuid ->
                async(Dispatchers.IO) {
                    remoteSummonerFacade.getSummonerByPuuid(puuid)
                }
            }.awaitAll().map { summoner ->
                summonerRepository.save(Summoner(summoner))
            }
        }
    }

    private fun getMatchesDetail(matchIds: MutableList<String>): MutableList<MatchDetailDto> {
        return toMatchDetailDto(matchIds.map { matchId ->
            remoteMatchFacade.getMatch(matchId)
        }.toMutableList())
    }

    private fun toMatchDetailDto(matchDto: MutableList<MatchDto>): MutableList<MatchDetailDto> {
        val matchDetailDto: MutableList<MatchDetailDto> = mutableListOf()

        matchDto.forEach { match ->
            val matchId = match.metadata.matchId
            val queueId = toQueueType(match.info.queueId)
            val gameDuration = match.info.gameDuration
            val participants = match.metadata.participants

            match.info.participants.forEach { participant ->
                matchDetailDto.add(MatchDetailDto(matchId, queueId, gameDuration, participants, participant))
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

    private fun isNotExistsSummoner(name: String): Boolean = !summonerRepository.existsSummonerByName(name)

    private fun isNotExistsMatch(matchId: String): Boolean = !matchRepository.existsByMatchId(matchId)

    private fun isNotExistsSummonerCheck(name: String) {
        if (isNotExistsSummoner(name)) {
            log.debug("${NOT_FOUND_SUMMONER.msg} in method existsSummonerCheck")
            throw CustomException(NOT_FOUND_SUMMONER)
        }
    }
}