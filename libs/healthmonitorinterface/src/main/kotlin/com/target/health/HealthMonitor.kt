package com.target.health

interface HealthMonitor {
    val name: String
    fun check(): HealthCheckResponse
}

