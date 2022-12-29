package com.server.glol.domain.affiliation.service

import com.server.glol.domain.affiliation.dto.AffiliationResponse
import com.server.glol.domain.affiliation.dto.SaveAffiliationDto
import com.server.glol.domain.affiliation.entities.Affiliation
import com.server.glol.domain.affiliation.repository.AffiliationCustomRepository
import com.server.glol.domain.affiliation.repository.AffiliationRepository
import com.server.glol.domain.summonerProfile.repository.SummonerProfileCustomRepository
import com.server.glol.domain.summonerProfile.repository.SummonerProfileRepository
import com.server.glol.domain.team.repository.TeamRepository
import com.server.glol.domain.team.service.TeamService
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AffiliationServiceImpl(
    private val affCusRepo: AffiliationCustomRepository,
    private val affiliationRepository: AffiliationRepository,
    private val teamRepository: TeamRepository,
    private val summonerProfileRepository: SummonerProfileRepository,
    private val summonerProfileCustomRepository: SummonerProfileCustomRepository,
    private val teamService: TeamService
) : AffiliationService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun getAffiliationByTag(
        queueType: String?,
        teamName: String?,
        pageable: Pageable
    ): Page<AffiliationResponse> {

        if (teamName != null) {
            teamService.teamExistsCheck(teamName = teamName)
        }

        return affCusRepo.getAffiliationByTag(queueType = queueType, teamName = teamName, pageable = pageable)
    }

    override fun saveAffiliation(saveAffiliationDto: SaveAffiliationDto) {

        if (isNotExistsCheckTeam(saveAffiliationDto.teamName)) {
            log.info("${ErrorCode.NOT_FOUND_TEAM.msg} by ${saveAffiliationDto.teamName}")
            throw CustomException(ErrorCode.NOT_FOUND_TEAM)
        }

        if (isNotExistsCheckSummonerProfile(saveAffiliationDto.summonerName)) {
            log.info("${ErrorCode.NOT_FOUND_SUMMONER_PROFILE.msg} by ${saveAffiliationDto.summonerName}")
            throw CustomException(ErrorCode.NOT_FOUND_SUMMONER_PROFILE)
        }

        val findTeam = teamRepository.findByName(saveAffiliationDto.teamName)

        val findSummonerProfile =
            summonerProfileCustomRepository.getHighRankSummonerProfileByName(saveAffiliationDto.summonerName)

        affiliationRepository.save(Affiliation(findTeam, findSummonerProfile))
    }

    private fun isNotExistsCheckTeam(teamName: String): Boolean = !teamRepository.existsByName(teamName)

    private fun isNotExistsCheckSummonerProfile(summonerName: String): Boolean =
        !summonerProfileRepository.existsBySummonerName(summonerName)
}