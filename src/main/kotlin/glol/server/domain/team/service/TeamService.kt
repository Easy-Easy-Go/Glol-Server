package com.server.glol.domain.team.service

interface TeamService {
    fun createTeam(teamName: String)
    fun teamExistsCheck(teamName: String): Boolean
}