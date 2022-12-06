package com.server.glol.match.facade

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.dto.riot.matchv5.ParticipantsDto
import com.server.glol.domain.match.service.facade.RemoteMatchFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_MATCH
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RemoteMatchTest : DescribeSpec({

    describe("getMatchIds는") {

        context("유효한 puuid와 matchPageable를 받는 경우") {
            every { remoteMatchFacade.getMatchIds(puuid, matchPageable) } returns matchIds

            val findMatchIds = remoteMatchFacade.getMatchIds(puuid, matchPageable)

            it("matchIds를 반환한다") {
                findMatchIds shouldBe matchIds
            }
        }

        context("유효하지 않은 인자를 받는 경우") {
            every { remoteMatchFacade.getMatchIds("", MatchPageable(0, 1)) } throws CustomException(NOT_FOUND_MATCH)

            it("Not found match exception을 던진다") {
                shouldThrow<CustomException> {
                    remoteMatchFacade.getMatchIds("", MatchPageable(0, 1))
                }
            }
        }
    }

    describe("getMatch는") {

        context("유효한 matchId를 받는 경우") {
            every { remoteMatchFacade.getMatch(matchId) } returns match

            val findMatch = remoteMatchFacade.getMatch(matchId)

            it("MatchDto를 반환한다") {
                findMatch shouldBe match
            }
        }

        context("존재하지 않는 matchId를 받는 경우") {
            every { remoteMatchFacade.getMatch(notValidMatchId) } throws CustomException(NOT_FOUND_MATCH)

            it("Not found match exception 던진다") {
                shouldThrow<CustomException> {
                    remoteMatchFacade.getMatch(notValidMatchId)
                }
            }
        }
    }


}) {
    companion object {
        private val puuid = "Cntz2MXELGgNAbLArhEpA-cQvhgNDcSGzDe2D4kouURrs0meLt2R4hR-oy1XZOrIUTReppExaFK8DA"
        private val matchPageable = MatchPageable(420, 4)
        private val matchIds: MutableList<String> = mutableListOf("KR_6226008135")
        private val match: MatchDto = MatchDto(ParticipantsDto("KR_6226008135"))
        private const val matchId = "KR_6226008135"
        private val notValidMatchId = ""
        val remoteMatchFacade = mockk<RemoteMatchFacade>()
    }
}

