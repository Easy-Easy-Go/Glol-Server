package com.server.glol.summoner.service

import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.repository.projection.SummonerDto
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.facade.RemoteSummonerFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class SummonerServiceTest: DescribeSpec({

    describe("registerSummoner가") {

        context("DB에 존재하지 않는 name을 받은 경우") {
            every { summonerRepository.existsSummonerByName(NAME) } returns false
            every { summonerService.registerSummoner(NAME) } returns SUMMONER_IDX

            summonerService.registerSummoner(NAME)

            it("소환사 등록에 성공한다") {
                verify(exactly = 1) { summonerService.registerSummoner(NAME) }
            }
        }

        context("DB에 존재하는 name을 받은 경우") {
            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { summonerService.registerSummoner(NAME) } throws CustomException(NOT_FOUND_SUMMONER)

            it("예외를 반환한다") {
                shouldThrow<CustomException> {
                    summonerService.registerSummoner(NAME)
                }
            }
        }
    }

    describe("getPuuid가") {

        context("DB에 name이 있을 경우") {
            every { summonerService.getPuuid(NAME) } returns PUUID

            val getPuuid = summonerService.getPuuid(NAME)

            it("puuid를 반환한다") {
                getPuuid shouldBe PUUID
            }
        }

        context("DB에 name이 없을 경우") {
            every { remoteSummoner.getSummonerByName(NAME) } returns summonerDto

            val getPuuid = remoteSummoner.getSummonerByName(NAME).puuid

            it("puuid를 반환한다") {
                getPuuid shouldBe summonerDto.puuid
            }
        }

        context("유효하지 않은 name일 경우") {
            every { summonerService.getPuuid(NAME) } throws CustomException(NOT_FOUND_SUMMONER)

            it("예외를 반환한다") {
                shouldThrow<CustomException> {
                    summonerService.getPuuid(NAME)
                }
            }
        }
    }

}) {

    companion object {
        private const val PUUID = "Cntz2MXELGgNAbLArhEpA-cQvhgNDcSGzDe2D4kouURrs0meLt2R4hR-oy1XZOrIUTReppExaFK8DA"
        private const val NAME = "권선징악어부"
        private const val SUMMONER_IDX = 1L
        private val summonerService = mockk<SummonerService>()
        private val summonerRepository = mockk<SummonerRepository>()
        private val remoteSummoner = mockk<RemoteSummonerFacade>()
        private val summonerDto = SummonerDto(puuid = PUUID)
    }
}