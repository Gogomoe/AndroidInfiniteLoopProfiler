package moe.gogo.report

import moe.gogo.Config
import moe.gogo.jfr.MemoryRecord
import java.io.File
import java.time.Duration

data class Result(
    val config: Config,
    val root: File,
    val moduleCall: ModuleCallResult,
    val ssacfgExtract: SSACFGExtractResult
)

data class ModuleCallResult(
    val dir: File,
    val costTime: Duration,
    val memory: MemoryRecord
) {
    val maxHeap: Long = memory.record.map { it.used }.max()!!
    val jfr: File = dir.resolve("recording.jfr")
    val dot: File = dir.resolve("ModuleCallGraph.dot")
}

data class SSACFGExtractResult(
    val dir: File,
    val costTime: Duration,
    val memory: MemoryRecord
) {
    val maxHeap: Long = memory.record.map { it.used }.max()!!
    val jfr: File = dir.resolve("recording.jfr")
}