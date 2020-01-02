package moe.gogo.report

import moe.gogo.Config
import moe.gogo.jfr.MemoryRecord
import java.io.File
import java.time.Duration

data class Result(
    val config: Config,
    val root: File,
    val moduleCallDir: File,
    val moduleCallTime: Duration,
    val moduleCallMemory: MemoryRecord,
    val ssacfgExtractDir: File,
    val ssacfgExtractTime: Duration,
    val ssacfgExtractMemory: MemoryRecord
) {
    val moduleCallMaxHeap: Long = moduleCallMemory.record.map { it.used }.max()!!
    val ssacfgExtractHeap: Long = ssacfgExtractMemory.record.map { it.used }.max()!!
}