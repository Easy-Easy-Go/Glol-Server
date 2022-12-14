package com.server.glol.domain.summoner.service.impl

import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
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

    override fun registerSummoner(name: String) {
        if (summonerRepository.existsSummonerByName(name)) {
            log.info(ErrorCode.ALREADY_EXISTS_SUMMONER.msg)
            throw CustomException(ErrorCode.ALREADY_EXISTS_SUMMONER)
        }

        val summoner = remoteSummonerFacade.getSummonerByName(name)

        summonerRepository.save(Summoner(summoner))
    }

    override fun getPuuid(name: String): String
            = summonerCustomRepository.findPuuidByName(name)
        ?: remoteSummonerFacade.getSummonerByPuuid(name).puuid
}