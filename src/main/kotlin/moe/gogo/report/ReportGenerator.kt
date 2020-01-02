package moe.gogo.report

class ReportGenerator(val result: Result) {

    val dist = result.root.resolve("report")

    private val templates: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{module-call-time}}" to { moduleCallTime.toMillis().toString() },
            "{{module-call-max-heap}}" to { moduleCallMaxHeap.toString() },
            "{{module-call-memory-chart-js}}" to {
                MemoryChartGenerator("module-call-memory", moduleCallMemory).generate()
            },
            "{{ssa-cfg-extract-time}}" to { ssacfgExtractTime.toMillis().toString() },
            "{{ssa-cfg-extract-max-heap}}" to { ssacfgExtractMaxHeap.toString() },
            "{{ssa-cfg-extract-memory-chart-js}}" to {
                MemoryChartGenerator("ssa-cfg-extract-memory", ssacfgExtractMemory).generate()
            }
        )
    }

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

}
