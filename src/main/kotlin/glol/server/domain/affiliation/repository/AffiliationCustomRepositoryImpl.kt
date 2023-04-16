package com.server.glol.domain.affiliation.repository

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.server.glol.domain.affiliation.dto.AffiliationResponse
import com.server.glol.domain.affiliation.dto.projection.QAffiliationDto
import com.server.glol.domain.affiliation.entities.QAffiliation.affiliation
import com.server.glol.domain.summoner.entities.QSummoner.summoner
import com.server.glol.domain.summonerProfile.entities.QSummonerProfile.summonerProfile
import com.server.glol.domain.team.entities.QTeam.team
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SORT
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils


@Repository
class AffiliationCustomRepositoryImpl(private val query: JPAQueryFactory) : AffiliationCustomRepository {
    override fun getAffiliationByTag(
        queueType: String?,
        teamName: String?,
        pageable: Pageable
    ): Page<AffiliationResponse> {
        val affiliations = query.from(affiliation)
            .select(
                QAffiliationDto(
                    summoner.name,
                    summoner.level,
                    summoner.profileIconId,
                    team.name,
                    summonerProfile.queueType,
                    summonerProfile.wins,
                    summonerProfile.losses,
                    summonerProfile.winRate,
                    summonerProfile.games,
                    summonerProfile.rank,
                    summonerProfile.tier
                )
            )
            .where(
                eqQueueType(queueType = queueType),
                eqTeamName(teamName = teamName)
            )
            .innerJoin(affiliation.summonerProfile, summonerProfile)
            .innerJoin(affiliation.team, team)
            .innerJoin(affiliation.summonerProfile.summoner, summoner)
            .orderBy(affiliationSort(pageable))
            .fetch()

        val count = query.select(affiliation.count())
            .from(affiliation)
            .where(eqTeamName(teamName))
            .innerJoin(affiliation.summonerProfile, summonerProfile)
            .fetchOne()

        return PageImpl(affiliations.map { AffiliationResponse(it) }, pageable, count!!)
    }

    private fun affiliationSort(pageable: Pageable): OrderSpecifier<Int> {

        for (order in pageable.sort) {
            val direction = if (order.direction.isAscending) Order.ASC else Order.DESC

            return when (order.property) {
                "wins" -> OrderSpecifier(direction, summonerProfile.wins)
                "winRate" -> OrderSpecifier(direction, summonerProfile.winRate)
                "losses" -> OrderSpecifier(direction, summonerProfile.losses)
                "level" -> OrderSpecifier(direction, summoner.level)
                "games" -> OrderSpecifier(direction, summonerProfile.games)
                "rankScore" -> OrderSpecifier(direction, summonerProfile.rankScore)

                else -> {
                    OrderSpecifier(direction, summoner.level)
                }
            }
        }
        throw CustomException(NOT_FOUND_SORT)
    }

    private fun eqQueueType(queueType: String?): BooleanExpression? {
        return if (!StringUtils.hasText(queueType)) null else summonerProfile.queueType.eq(queueType)
    }

    private fun eqTeamName(teamName: String?): BooleanExpression? {
        return if (!StringUtils.hasText(teamName)) null else team.name.eq(teamName)
    }
}