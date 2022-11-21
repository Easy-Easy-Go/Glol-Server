package com.server.glol.domain.summoner.service.impl

import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.repository.projection.SummonerDto
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.SummonerServiceFacade
import com.server.glol.global.config.banned.BannedAccountConfig
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SummonerServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val summonerServiceFacade: SummonerServiceFacade,
) : SummonerService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun registerSummonerByName(name: String) {
        if (summonerRepository.existsSummonerByName(name)) {
            log.info(ErrorCode.ALREADY_EXISTS_SUMMONER.msg)
            throw CustomException(ErrorCode.ALREADY_EXISTS_SUMMONER)
        }

        val summoner = summonerServiceFacade.getSummonerByName(name)

        saveByName(summoner)
    }

    private fun saveByName(summoner: SummonerDto) {
        if (summoner.name != BannedAccountConfig.name)
            summonerRepository.save(Summoner(summoner))
    }
}