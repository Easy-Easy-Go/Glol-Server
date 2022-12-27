package com.server.glol.domain.team.service.impl

import com.server.glol.domain.summonerProfile.repository.SummonerProfileRepository
import com.server.glol.domain.team.dto.RegisterTeamDto
import com.server.glol.domain.team.entities.Team
import com.server.glol.domain.team.repository.TeamRepository
import com.server.glol.domain.team.service.TeamService
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TeamServiceImpl(
    private val teamRepository: TeamRepository,
    private val summonerProfileRepository: SummonerProfileRepository,
) : TeamService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun createTeam(dto: RegisterTeamDto) {

        if (isExistsTeam(dto.teamName)) {
            log.info("${ALREADY_EXISTS_TEAM.msg} by ${dto.teamName}")
            throw CustomException(ALREADY_EXISTS_TEAM)
        }

        if (leagueNotExists(dto.summonerProfileIdx)) {
            log.info("${NOT_FOUND_SUMMONER_PROFILE.msg} by ${dto.summonerProfileIdx}")
            throw CustomException(NOT_FOUND_SUMMONER_PROFILE)
        }

        teamRepository.save(Team(name = dto.teamName))
    }

    override fun teamExistsCheck(teamName: String): Boolean {
        if (isNotExistsTeam(teamName)) {
            log.info("${ALREADY_EXISTS_TEAM.msg} by $teamName")
            throw CustomException(ALREADY_EXISTS_TEAM)
        }

        return false
    }

    private fun isNotExistsTeam(name: String): Boolean = !teamRepository.existsByName(name)
    private fun isExistsTeam(name: String): Boolean = teamRepository.existsByName(name)
    private fun leagueNotExists(idx: Long): Boolean = !summonerProfileRepository.existsByIdx(idx)

}