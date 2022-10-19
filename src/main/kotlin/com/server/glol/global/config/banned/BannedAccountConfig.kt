package com.server.glol.global.config.banned

import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class BannedAccountConfig(val summonerRepository: SummonerRepository) {

    @PostConstruct
    fun suspensionAccountSave() {
        if (!summonerRepository.existsSummonerByName("Banned Account")) {
            summonerRepository.save(Summoner("bannedId", "bannedAccountId", "Banned Account", "bannedPuuid", 0, true))
        }
    }
}