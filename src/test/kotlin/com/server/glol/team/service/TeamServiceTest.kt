package com.server.glol.team.service

import com.server.glol.domain.team.dto.RegisterTeamDto
import com.server.glol.domain.team.entities.Team
import com.server.glol.domain.team.repository.TeamRepository
import com.server.glol.domain.team.service.TeamService
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.ALREADY_EXISTS_TEAM
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

class TeamServiceTest : DescribeSpec({

    describe("registerTeam이") {

        context("유효한 teamName을 받는 경우") {
            every { teamService.createTeam(TEAM_NAME) } just Runs
            every { teamRepository.findByName(teamDto.teamName) } returns saveTeam

            teamService.createTeam(TEAM_NAME)

            val findTeam = teamRepository.findByName(teamDto.teamName)

            it("팀이 등록된다") {
                findTeam shouldBe saveTeam
            }
        }

        context("이미 Team이 존재할 때") {
            every { teamRepository.existsByName(notValidDto.teamName) } returns true
            every { teamService.createTeam(TEAM_NAME) } throws CustomException(ALREADY_EXISTS_TEAM)

            it("예외가 발생한다") {
                shouldThrow<CustomException> {
                    teamService.createTeam(TEAM_NAME)
                }
            }
        }
    }

    describe("teamExistsCheck가") {

        context("teamName이 존재할 때") {
            every { teamRepository.existsByName(TEAM_NAME) } returns true
            every { teamService.teamExistsCheck(TEAM_NAME) } returns false

            it("false를 반환한다") {
                teamService.teamExistsCheck(TEAM_NAME) shouldBe false
            }
        }

        context("teamName이 존재하지 않을 때") {
            every { teamRepository.existsByName(TEAM_NAME) } returns false
            every { teamService.teamExistsCheck(TEAM_NAME) } returns true

            it("true를 반환한다") {
                teamService.teamExistsCheck(TEAM_NAME) shouldBe true
            }
        }
    }
}) {

    companion object {
        private const val TEAM_NAME = "GSM"
        private val saveTeam = Team(TEAM_NAME)
        private val teamDto = RegisterTeamDto(TEAM_NAME, 2L)
        private val notValidDto = RegisterTeamDto(TEAM_NAME, 0L)

        private val teamService = mockk<TeamService>()
        private val teamRepository = mockk<TeamRepository>()
    }
}