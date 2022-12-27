package com.server.glol.league.facade

import com.server.glol.domain.summonerProfile.dto.SummonerProfileDto
import com.server.glol.domain.summonerProfile.service.facade.RemoteSummonerProfileFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER_PROFILE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RemoteSummonerProfileFacadeTest: DescribeSpec({

    describe("getSummonerProfile 가") {

        context("유효한 name을 받을 경우") {
            every { remoteLeague.getSummonerProfile(NAME) } returns summonerProfileDtos

            val getLeague = remoteLeague.getSummonerProfile(NAME)

            it("leagueDto를 반환한다") {
                getLeague shouldBe summonerProfileDtos
            }
        }

        context("유효하지 않은 name을 받을 경우") {
            every { remoteLeague.getSummonerProfile(NAME) } throws CustomException(NOT_FOUND_SUMMONER_PROFILE)

            it("Not found league exception을 던진다") {
                shouldThrow<CustomException> {
                    remoteLeague.getSummonerProfile(NAME)
                }
            }
        }
    }

}) {
    companion object {
        private const val NAME = "권선징악어부"
        private val summonerProfileDtos = mutableSetOf(
            SummonerProfileDto(queueType = "SOLO_RANK", summonerName = NAME),
            SummonerProfileDto(queueType = "FREE_RANK", summonerName = NAME)
        )
        private val remoteLeague = mockk<RemoteSummonerProfileFacade>()
    }
}