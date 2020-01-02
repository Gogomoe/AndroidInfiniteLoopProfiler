package moe.gogo.report

import moe.gogo.jfr.HeapSample
import moe.gogo.jfr.MemoryRecord
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class ReportGenerator(val dist: File, val memoryRecord: MemoryRecord) {

    private val templates: List<Pair<String, () -> String>> = listOf(
        "{{memory-committed}}" to { memoryRecord.buildCommittedReport() },
        "{{memory-used}}" to { memoryRecord.buildUsedReport() }
    )

    fun generate() {
        if (!dist.exists()) {
            dist.mkdir()
        }

        var report = String(getResource("Report.html").readAllBytes())
        templates.forEach { (template, result) ->
            report = report.replace(template, result())
        }
        dist.resolve("Report.html").writeBytes(report.toByteArray())
    }

    private fun copy(filename: String) {
        Files.copy(getResource(filename), dist.toPath().resolve(filename), StandardCopyOption.REPLACE_EXISTING)
    }

    private fun getResource(filename: String) = this::class.java.classLoader.getResourceAsStream(filename)!!
}


private fun MemoryRecord.buildCommittedReport(): String {
    val list = mutableListOf<HeapSample>()
    list.add(record.first())
    for (i in 1 until record.size - 1) {
        val last = record.get(i - 1)
        val next = record.get(i + 1)
        val sample = record.get(i)
        if (sample.committed != last.committed || sample.committed != next.committed) {
            list.add(sample)
        }
    }
    list.add(record.last())

    return list.map {
        "[${it.time.toEpochMilli()}, ${it.committed}]"
    }.joinToString(",")
}

private fun MemoryRecord.buildUsedReport(): String {
    return this.record.map {
        "[${it.time.toEpochMilli()}, ${it.used}]"
    }.joinToString(",")
}
