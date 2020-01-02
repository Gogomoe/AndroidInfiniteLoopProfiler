package moe.gogo.report

import moe.gogo.jfr.HeapSample
import moe.gogo.jfr.MemoryRecord

class MemoryChartGenerator(val id: String, memoryRecord: MemoryRecord) {

    private val templates: List<Pair<String, () -> String>> = listOf(
        "{{memory-chart-id}}" to { id },
        "{{memory-committed}}" to { memoryRecord.buildCommittedReport() },
        "{{memory-used}}" to { memoryRecord.buildUsedReport() }
    )

    fun generate(): String {

        var chartJs = String(getResource("memory-chart-tamplate.js").readAllBytes())
        templates.forEach { (template, result) ->
            chartJs = chartJs.replace(template, result())
        }

        return chartJs
    }
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
