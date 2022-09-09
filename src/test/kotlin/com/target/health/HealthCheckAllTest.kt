package com.target.health

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import org.junit.jupiter.api.Test

@OptIn(DelicateCoroutinesApi::class)
class HealthCheckAllTest {

    @Test
    fun `test healthCheckAll empty`() {
        val result = healthCheckAll(emptyList())

        result.size shouldBe 0
    }

    @Test
    fun `test healthCheckAll`() {
        val monitor1 = mockk<HealthMonitor>()
        val monitor2 = mockk<HealthMonitor>()
        every { monitor1.check() } returns(HealthCheckResponse("Monitor1", true, "Monitor 1 is healthy"))
        every { monitor2.check() } returns(HealthCheckResponse("Monitor2", false, "Monitor 2 is healthy"))

        val result = healthCheckAll(listOf(monitor1, monitor2))

        result.size shouldBe 2
        result[0].name shouldBe "Monitor1"
        result[1].name shouldBe "Monitor2"
    }

    @Test
    fun `test healthCheckAll with custom context`() {
        val monitor1 = mockk<HealthMonitor>()
        val monitor2 = mockk<HealthMonitor>()
        every { monitor1.check() } answers { HealthCheckResponse("Monitor1", true, "Monitor 1 is healthy on ${Thread.currentThread()}")}
        every { monitor2.check() } answers { HealthCheckResponse("Monitor2", true, "Monitor 2 is healthy on ${Thread.currentThread()}")}

        val context = newSingleThreadContext(name = "TestContext")

        val result = healthCheckAll(listOf(monitor1, monitor2), context)

        assertSoftly {
            result.size shouldBe 2
            result[0].name shouldBe "Monitor1"
            result[0].details shouldStartWith "Monitor 1 is healthy on Thread[TestContext @coroutine#"
            result[1].name shouldBe "Monitor2"
            result[1].details shouldStartWith "Monitor 2 is healthy on Thread[TestContext @coroutine#"
        }
    }

    @Test
    fun `test allFailedChecks empty`() {
        val result = allFailedChecks(emptyList())

        result.size shouldBe 0
    }

    @Test
    fun `test allFailedChecks`() {
        val monitor1 = mockk<HealthMonitor>()
        val monitor2 = mockk<HealthMonitor>()
        every { monitor1.check() } returns(HealthCheckResponse("Monitor1", true, "Monitor 1 is healthy"))
        every { monitor2.check() } returns(HealthCheckResponse("Monitor2", false, "Monitor 2 is healthy"))

        val result = allFailedChecks(listOf(monitor1, monitor2))

        result.size shouldBe 1
        result[0].name shouldBe "Monitor2"
    }

    @Test
    fun `test allFailedChecks multiple failure`() {
        val monitor1 = mockk<HealthMonitor>()
        val monitor2 = mockk<HealthMonitor>()
        every { monitor1.check() } returns(HealthCheckResponse("Monitor1", false, "Monitor 1 is healthy"))
        every { monitor2.check() } returns(HealthCheckResponse("Monitor2", false, "Monitor 2 is healthy"))

        val result = healthCheckAll(listOf(monitor1, monitor2))

        result.size shouldBe 2
        result[0].name shouldBe "Monitor1"
        result[1].name shouldBe "Monitor2"
    }
}
