package league.facade

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_LEAGUE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RemoteLeagueFacadeTest: DescribeSpec({

    describe("getLeague가") {

        context("유효한 name을 받을 경우") {
            every { remoteLeague.getLeague(NAME) } returns leagueDto

            val getLeague = remoteLeague.getLeague(NAME)

            it("leagueDto를 반환한다") {
                getLeague shouldBe leagueDto
            }
        }

        context("유효하지 않은 name을 받을 경우") {
            every { remoteLeague.getLeague(NAME) } throws CustomException(NOT_FOUND_LEAGUE)

            it("Not found league exception을 던진다") {
                shouldThrow<CustomException> {
                    remoteLeague.getLeague(NAME)
                }
            }
        }
    }

}) {
    companion object {
        private const val NAME = "권선징악어부"
        private val leagueDto = mutableSetOf(
            LeagueDto(queueType = "SOLO_RANK", summonerName = NAME),
            LeagueDto(queueType = "FREE_RANK", summonerName = NAME)
        )
        private val remoteLeague = mockk<RemoteLeagueFacade>()
    }
}