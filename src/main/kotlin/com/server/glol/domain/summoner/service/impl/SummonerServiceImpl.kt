package com.server.glol.domain.summoner.service.impl

import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.repository.projection.SummonerDto
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.facade.RemoteSummonerFacade
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SummonerServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val remoteSummonerFacade: RemoteSummonerFacade,
    private val summonerCustomRepository: SummonerCustomRepository,
) : SummonerService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun registerSummoner(name: String): Long {
        if (isExistsSummoner(name)) {
            log.debug("${ErrorCode.ALREADY_EXISTS_SUMMONER.msg} in RegisterSummoner Method")
            throw CustomException(ErrorCode.ALREADY_EXISTS_SUMMONER)
        }

        return summonerSave(summoner = getSummonerByName(name))!!
    }

    override fun getPuuid(name: String): String = summonerCustomRepository.findPuuidByName(name)
        ?: remoteSummonerFacade.getSummonerByPuuid(name).puuid

    private fun isExistsSummoner(name: String): Boolean = summonerRepository.existsSummonerByName(name)

    private fun getSummonerByName(name: String) = remoteSummonerFacade.getSummonerByName(name)

    private fun summonerSave(summoner: SummonerDto) =
        summonerRepository.save(Summoner(summonerDto = summoner)).idx
}