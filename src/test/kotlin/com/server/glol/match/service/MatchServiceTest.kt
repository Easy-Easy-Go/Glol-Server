package com.server.glol.match.service

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.MetadataDto
import com.server.glol.domain.match.dto.projection.MatchesDto
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.dto.riot.matchv5.ParticipantsDto
import com.server.glol.domain.match.repository.MatchRepository
import com.server.glol.domain.match.service.MatchService
import com.server.glol.domain.match.service.facade.RemoteMatchFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_MATCH
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class MatchServiceTest : DescribeSpec({

    describe("getMatch는") {

        context("DB에 존재하는 matchId를 받는 경우") {
            every { matchService.getMatch(MATCH_ID) } returns matchResponse

            val getMatchResponse = matchService.getMatch(MATCH_ID)

            it("matchResponse를 반환한다") {
                getMatchResponse shouldBe matchResponse
            }
        }

        context("DB에 존재하지 않는 matchId를 받는 경우 외부 API를 실행하여") {
            val notExistsMatchId = "KR_0000000000"

            every { matchRepository.existsByMatchId(notExistsMatchId) } returns false

            context("해당 matchId가 존재할 때") {
                every { remoteMatch.getMatch(notExistsMatchId) } returns matchDto

                val getMatchDto = remoteMatch.getMatch(notExistsMatchId)

                it("MatchDto를 반환한다") {
                    getMatchDto shouldBe matchDto
                }
            }

            context("해당 matchId가 존재하지 않을 때") {
                every { remoteMatch.getMatch(notExistsMatchId) } throws CustomException(NOT_FOUND_MATCH)

                it("예외를 반환한다") {
                    shouldThrow<CustomException> {
                        remoteMatch.getMatch(notExistsMatchId)
                    }
                }
            }
        }
    }

    describe("getMatches가") {

        val pageable = Pageable.ofSize(1)
        val count = 5L
        val matchesDto: Page<MatchesDto> = PageImpl(mutableListOf(MatchesDto()), pageable, count)

        context("유효한 name, matchPageable을 받는 경우") {

            every { matchService.getMatches(NAME, matchPageable, pageable) } returns matchesDto

            val getMatchesDto = matchService.getMatches(NAME, matchPageable, pageable)

            it("matchesDto 를 반환한다") {
                getMatchesDto shouldBe matchesDto
            }
        }

        context("존재하지 않는 name을 받는 경우") {
            val notExistsName = "NotExistsName"

            every {
                matchService.getMatches(notExistsName, matchPageable, pageable)
            } throws CustomException(NOT_FOUND_SUMMONER)

            it("예외를 반환한다") {
                shouldThrow<CustomException> {
                    matchService.getMatches(notExistsName, matchPageable, pageable)
                }
            }
        }
    }

    describe("renewalMatches가") {

        context("존재하는 name을 받는 경우") {
            every { matchService.renewalMatches(NAME, matchPageable) } just Runs

            matchService.renewalMatches(NAME, matchPageable)

            it("전적 갱신이 성공한다") {

                verify(exactly = 1) { matchService.renewalMatches(NAME, matchPageable) }
            }
        }
    }
}) {
    companion object {
        private const val MATCH_ID = "KR_6226008135"
        private const val NAME = "권선징악어부"
        val matchPageable = MatchPageable(queue = 420, count = 0)
        private val matchResponse = MatchResponse(MetadataDto(MATCH_ID, "SOLO_RANK", 1000), mutableListOf())
        private val matchDto: MatchDto = MatchDto(ParticipantsDto("KR_6226008135"))

        private val matchService = mockk<MatchService>()
        private val remoteMatch = mockk<RemoteMatchFacade>()
        private val matchRepository = mockk<MatchRepository>()
    }
}