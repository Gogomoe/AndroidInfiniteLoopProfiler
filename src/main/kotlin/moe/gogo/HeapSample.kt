package moe.gogo

import java.time.Instant

data class HeapSample(
    val time: Instant,
    val committed: Long,
    val used: Long
)