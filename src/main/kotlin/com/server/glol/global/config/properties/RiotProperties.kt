package com.server.glol.global.config.properties

class RiotProperties{

        companion object {
                const val SUMMONER_API_URL: String = "https://kr.api.riotgames.com/lol/summoner/v4/summoners"
                const val SUMMONER_BY_PUUID_URL: String = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"
                const val MATCH_UUID_URL: String = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"
                const val MATCHES_MATCH_ID_URL: String = "https://asia.api.riotgames.com/lol/match/v5/matches/"
                const val SUMMONER_PROFILE_ACCOUNT_URL: String = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/"
                const val ORIGIN: String = "https://developer.riotgames.com"
        }
}