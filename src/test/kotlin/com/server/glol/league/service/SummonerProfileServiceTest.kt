package com.server.glol.league.service

import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.service.SummonerProfileService
import com.server.glol.domain.summonerProfile.service.facade.RemoteSummonerProfileFacade
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER_PROFILE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class SummonerProfileServiceTest : DescribeSpec({

    describe("getSummonerProfile이") {

        context("DB에 존재하는 name을 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { summonerProfileService.getSummonerProfile(NAME) } returns summonerProfileResponses

            val getSummonerProfile = summonerProfileService.getSummonerProfile(NAME)

            it("summonerProfileResponses를 반환합니다") {
                getSummonerProfile shouldBe summonerProfileResponses
            }
        }

        context("DB에 존재하지 않는 name은 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns false
            every { remoteLeague.getSummonerProfile(NAME) } throws CustomException(NOT_FOUND_SUMMONER_PROFILE)

            it("Not found SummonerPrfile exception을 반환합니다") {
                shouldThrow<CustomException> {
                    remoteLeague.getSummonerProfile(NAME)
                }
            }
        }
    }

    describe("saveSummonerProfile이") {

        context("DB에 존재하는 name을 받은 경우") {
            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { summonerProfileService.saveSummonerProfile(NAME) } just Runs

            summonerProfileService.saveSummonerProfile(NAME)

            it("summonerProfile가 save된다") {
                verify(exactly = 1) { summonerProfileService.saveSummonerProfile(NAME) }
            }
        }
    }
}) {

    companion object {
        private const val NAME = "권선징악어부"
        private val summonerProfileResponses = mutableSetOf(
            SummonerProfileResponse(queueType = "SOLO_RANK", summonerName = NAME),
            SummonerProfileResponse(queueType = "FREE_RANK", summonerName = NAME)
        )

        private val summonerRepository = mockk<SummonerRepository>()
        private val remoteLeague = mockk<RemoteSummonerProfileFacade>()
        private val summonerProfileService = mockk<SummonerProfileService>()
    }
}