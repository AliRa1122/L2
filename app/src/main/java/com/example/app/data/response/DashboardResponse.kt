package com.example.app.data.response

import com.example.app.models.Entity

// Data classes for API requests and responses
data class DashboardResponse(
    val entities: List<Entity>,
    val entityTotal: Int
)
