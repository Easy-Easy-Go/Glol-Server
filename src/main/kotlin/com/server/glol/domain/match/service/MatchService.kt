package com.server.glol.domain.match.service

import com.server.glol.domain.match.repository.projection.MatchResponse

interface MatchService {
    fun renewalMatches(name: String, queue: Int, count: Int)
    fun getMatches(name: String, queue: Int, count: Int): MutableList<MatchResponse>?
}