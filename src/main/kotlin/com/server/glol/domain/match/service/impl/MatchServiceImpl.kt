package com.server.glol.domain.match.service.impl

import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.match.dto.*
import com.server.glol.domain.match.dto.projection.AllMatchVo
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.entities.Champion
import com.server.glol.domain.match.entities.Item
import com.server.glol.domain.match.entities.Match
import com.server.glol.domain.match.repository.ChampionRepository
import com.server.glol.domain.match.repository.ItemRepository
import com.server.glol.domain.match.repository.MatchCustomRepository
import com.server.glol.domain.match.repository.MatchRepository
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.match.service.MatchServiceFacade
import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.RemoteSummonerFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val summonerCustomRepository: SummonerCustomRepository,
    private val matchCustomRepository: MatchCustomRepository,
    private val matchRepository: MatchRepository,
    private val itemRepository: ItemRepository,
    private val matchServiceFacade: MatchServiceFacade,
    private val championRepository: ChampionRepository,
    private val leagueService: LeagueService,
    private val remoteSummonerFacade: RemoteSummonerFacade,
) : MatchService {

    @Transactional
    override fun renewalMatches(name: String, matchPageable: MatchPageable) {
        if(!summonerRepository.existsSummonerByName(name))
            throw CustomException(ErrorCode.NOT_FOUND_SUMMONER)

        val puuid = getPuuid(name)

        val matchIds = matchServiceFacade.getMatchIds(puuid, matchPageable)
            .filterNot {
                matchRepository.existsByMatchId(it)
            }.toMutableList()

        val matchDetail = getMatchesDetail(matchIds)

        matchesSave(matchDetail)

        leagueService.saveLeague(name)
    }

    override fun getMatch(matchId: String): MatchResponse {
        if (!matchRepository.existsByMatchId(matchId)) {
            return toMatchResponse(matchServiceFacade.getMatch(matchId))
        }

        return matchCustomRepository.findMatchesByMatchIds(matchId)!!
    }

    override fun getMatches(name: String, matchPageable: MatchPageable, pageable: Pageable): Page<AllMatchVo> {
        if (!summonerRepository.existsSummonerByName(name)) {
            throw CustomException(ErrorCode.NOT_FOUND_SUMMONER)
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
        val matchInfo: MutableList<MatchInfoDto> = mutableListOf()

        val metadata = MetadataDto(
            matchId = match.metadata.matchId,
            queueId = toQueueType(match.info.queueId),
            gameDuration = match.info.gameDuration
        )

        match.info.participants.forEach { participantDto ->
            matchInfo.add(MatchInfoDto(participantDto))
        }

        return MatchResponse(metadata, matchInfo)
    }

    private fun matchesSave(matches: MutableList<MatchDetailDto>) {
        runBlocking {
            matches.forEach { matchDetail ->
                notExistsSummonerSave(matchDetail)

                val summoner = async(Dispatchers.IO) {
                    summonerRepository.findSummonerByName(matchDetail.name)
                }

                val match = matchRepository.save(Match(matchDetail, summoner.await()))

                itemRepository.save(Item(matchDetail, match))
                championRepository.save(Champion(matchDetail, match))
            }
        }
    }

    private fun notExistsSummonerSave(match: MatchDetailDto) {
        runBlocking {
            match.participantsPuuid.filterNot { puuid ->
                summonerRepository.existsSummonerByPuuid(puuid)
            }.map { puuid ->
                async(Dispatchers.IO) {
                    remoteSummonerFacade.getSummonerByPuuid(puuid)
                }
            }.awaitAll().map { summoner ->
                async(Dispatchers.IO) {
                    summonerRepository.save(Summoner(summoner))
                }
            }.awaitAll().toMutableList()
        }
    }
    private fun getId(name: String): String
        = summonerCustomRepository.findIdByName(name)
            ?: remoteSummonerFacade.getSummonerByName(name).id


    private fun getPuuid(name: String): String
        = summonerCustomRepository.findPuuidByName(name)
            ?: remoteSummonerFacade.getSummonerByPuuid(name).puuid

    private fun getMatchesDetail(matchIds: MutableList<String>): MutableList<MatchDetailDto> {
        return toMatchDetailDto(matchIds.map { matchId ->
            matchServiceFacade.getMatch(matchId)
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
}