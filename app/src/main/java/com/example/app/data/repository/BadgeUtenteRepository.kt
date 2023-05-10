package com.example.app.data.repository

import com.example.app.data.dao.BadgeUtenteDAO
import com.example.app.data.entity.BadgeUtente
import kotlinx.coroutines.flow.Flow

class BadgeUtenteRepository(private val badgeUtenteDAO: BadgeUtenteDAO) {

    val badgeUtenti: Flow<List<BadgeUtente>> = badgeUtenteDAO.getBadges()
}