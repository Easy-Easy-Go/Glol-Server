package com.server.glol.global.config.banned

import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class BannedAccountConfig(val summonerRepository: SummonerRepository) {

    companion object {
        const val name = "Banned Account"
        const val id = "bannedId"
        const val accountId = "bannedAccountId"
        const val puuid = "bannedPuuid"
        const val profileIcon = 0
        const val visited = true
    }
    @PostConstruct
    fun suspensionAccountSave() {


        if (!summonerRepository.existsSummonerByName(name)) {
            summonerRepository.save(Summoner(id, accountId, name, puuid, profileIcon, visited))
        }
    }
}