package moe.gogo.report

import moe.gogo.jfr.HeapSample
import moe.gogo.jfr.MemoryRecord

class MemoryChartRender(private val id: String, private val memoryRecord: MemoryRecord) : ReportRender() {

    override val template: String = readString("memory-chart-tamplate.js")

    override val data: List<Pair<String, () -> String>> = listOf(
        "{{memory-chart-id}}" to { id },
        "{{memory-committed}}" to { memoryRecord.buildCommittedReport() },
        "{{memory-used}}" to { memoryRecord.buildUsedReport() }
    )

    private fun MemoryRecord.buildCommittedReport(): String {
        val list = mutableListOf<HeapSample>()
        list.add(record.first())
        for (i in 1 until record.size - 1) {
            val last = record[i - 1]
            val next = record[i + 1]
            val sample = record[i]
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


}

