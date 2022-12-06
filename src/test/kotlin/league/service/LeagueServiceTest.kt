package league.service

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.domain.summoner.repository.SummonerRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class LeagueServiceTest : DescribeSpec({

    describe("getLeague가") {

        context("DB에 존재하는 name을 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns true
            every { leagueService.getLeague(NAME) } returns leagueDto

            val getLeague = leagueService.getLeague(NAME)

            it("leagueDto를 반환합니다") {
                getLeague shouldBe leagueDto
            }
        }

        context("DB에 존재하지 않는 name은 받은 경우") {

            every { summonerRepository.existsSummonerByName(NAME) } returns false
            every { remoteLeague.getLeague(NAME) } returns leagueDto

            val getLeague = remoteLeague.getLeague(NAME)

            it("leagueDto를 반환합니다") {
                getLeague shouldBe leagueDto
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
        private val leagueDto = mutableSetOf(
            LeagueDto(queueType = "SOLO_RANK", summonerName = NAME),
            LeagueDto(queueType = "FREE_RANK", summonerName = NAME)
        )

        private val summonerRepository = mockk<SummonerRepository>()
        private val remoteLeague = mockk<RemoteLeagueFacade>()
        private val leagueService = mockk<LeagueService>()
    }
}