package com.target.health

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/**
 * Checks all monitors asynchronously with a default coroutineContext of Dispatchers.IO.
 */
fun healthCheckAll(
    healthMonitors: List<HealthMonitor>,
    coroutineContext: CoroutineContext = Dispatchers.IO
): List<HealthCheckResponse> {
    return runBlocking(context = coroutineContext) {
        healthMonitors.map { monitor ->
            async {
                try {
                    monitor.check()
                } catch (t: Throwable) {
                    HealthCheckResponse(
                        name = monitor.name,
                        isHealthy = false,
                        details = "${t::class.qualifiedName} - ${t.message}"
                    )
                }
            }
        }.awaitAll()
    }
}

fun allFailedChecks(
    healthMonitors: List<HealthMonitor>,
    coroutineContext: CoroutineContext = Dispatchers.IO
): List<HealthCheckResponse> = healthCheckAll(healthMonitors, coroutineContext).filter { !it.isHealthy }