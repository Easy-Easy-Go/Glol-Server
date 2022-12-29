package com.server.glol.summoner.facade

import com.server.glol.domain.summoner.dto.projection.SummonerDto
import com.server.glol.domain.summoner.service.facade.RemoteSummonerFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RemoteSummonerFacadeTest: DescribeSpec({

    describe("getSummonerByName가") {

        context("유효한 name을 받을 경우") {
            every { remoteSummoner.getSummonerByName(NAME) } returns summonerDto

            val getSummoner = remoteSummoner.getSummonerByName(NAME)

            it("summonerDto를 반환한다") {
                getSummoner shouldBe summonerDto
            }
        }

        context("유효하지 않은 name을 받을 경우") {
            every { remoteSummoner.getSummonerByName(NAME) } throws CustomException(NOT_FOUND_SUMMONER)

            it("예외를 반환한다") {
                shouldThrow<CustomException> {
                    remoteSummoner.getSummonerByName(NAME)
                }
            }
        }

        context("유효한 puuid를 받을 경우") {
            every { remoteSummoner.getSummonerByPuuid(PUUID) } returns summonerDto

            val getSummoner = remoteSummoner.getSummonerByPuuid(PUUID)

            it("summonerDto를 반환한다") {
                getSummoner shouldBe summonerDto
            }
        }

        context("유효하지 않은 puuid을 받을 경우") {
            every { remoteSummoner.getSummonerByPuuid(PUUID) } throws CustomException(NOT_FOUND_SUMMONER)

            it("예외를 반환한다") {
                shouldThrow<CustomException> {
                    remoteSummoner.getSummonerByPuuid(PUUID)
                }
            }
        }


    }
}) {
    companion object {
        private val NAME = "권선징악어부"
        private val PUUID = "Cntz2MXELGgNAbLArhEpA-cQvhgNDcSGzDe2D4kouURrs0meLt2R4hR-oy1XZOrIUTReppExaFK8DA"
        private val summonerDto = SummonerDto(name = NAME, puuid = PUUID)
        private val remoteSummoner = mockk<RemoteSummonerFacade>()
    }
}