package com.server.glol.league.service

import com.server.glol.domain.league.dto.projection.LeagueResponse
import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_LEAGUE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class LeagueServiceTest : DescribeSpec({

    describe("getLeague가") {

        context("DB에 존재하는 name을 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { leagueService.getLeague(NAME) } returns leagueResponse

            val getLeague = leagueService.getLeague(NAME)

            it("leagueResponse를 반환합니다") {
                getLeague shouldBe leagueResponse
            }
        }

        context("DB에 존재하지 않는 name은 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns false
            every { remoteLeague.getLeague(NAME) } throws CustomException(NOT_FOUND_LEAGUE)

            it("Not found League exception을 반환합니다") {
                shouldThrow<CustomException> {
                    remoteLeague.getLeague(NAME)
                }
            }
        }
    }

    describe("saveLeague가") {

        context("DB에 존재하는 name을 받은 경우") {
            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { leagueService.saveLeague(NAME) } just Runs

            leagueService.saveLeague(NAME)

            it("league가 save된다") {
                verify(exactly = 1) { leagueService.saveLeague(NAME) }
            }
        }
    }
}) {

    companion object {
        private const val NAME = "권선징악어부"
        private val leagueResponse = mutableSetOf(
            LeagueResponse(queueType = "SOLO_RANK", summonerName = NAME),
            LeagueResponse(queueType = "FREE_RANK", summonerName = NAME)
        )

        private val summonerRepository = mockk<SummonerRepository>()
        private val remoteLeague = mockk<RemoteLeagueFacade>()
        private val leagueService = mockk<LeagueService>()
    }
}