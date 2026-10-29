package com.target.health

data class HealthCheckResponse(
    val name: String,
    val isHealthy: Boolean,
    val details: String
)