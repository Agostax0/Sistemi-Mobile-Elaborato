package com.example.app.data.repository

import com.example.app.data.dao.BadgeRistoranteDAO
import com.example.app.data.entity.BadgeRistorante
import kotlinx.coroutines.flow.Flow

class BadgeRistoranteRepository(private val badgeRistoranteDAO: BadgeRistoranteDAO) {

    val badgeRistoranti: Flow<List<BadgeRistorante>> = badgeRistoranteDAO.getBadges()
}