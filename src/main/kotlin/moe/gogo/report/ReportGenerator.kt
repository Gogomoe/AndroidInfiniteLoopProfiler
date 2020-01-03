package moe.gogo.report

class ReportGenerator(val result: Result) {

    val dist = result.root.resolve("report")

    private val templates: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{module-call-time}}" to { moduleCall.costTime.toMillis().toString() },
            "{{module-call-max-heap}}" to { moduleCall.maxHeap.toString() },
            "{{module-call-dot-graph-js}}" to {
                DotGraphRender("module-call-dot-graph", moduleCall.dot.readText()).render()
            },
            "{{module-call-memory-chart-js}}" to {
                MemoryChartGenerator("module-call-memory", moduleCall.memory).generate()
            },
            "{{module-call-flame-graph}}" to {
                FlameGraphGenerator(config, moduleCall.jfr).generate()
            },
            "{{ssa-cfg-extract-time}}" to { ssacfgExtract.costTime.toMillis().toString() },
            "{{ssa-cfg-extract-max-heap}}" to { ssacfgExtract.maxHeap.toString() },
            "{{ssa-cfg-extract-graph-js}}" to {
                DotGraphRender("ssa-cfg-extract-dot-graph", ssacfgExtract.dot.readText()).render()
            },
            "{{ssa-cfg-extract-memory-chart-js}}" to {
                MemoryChartGenerator("ssa-cfg-extract-memory", ssacfgExtract.memory).generate()
            },
            "{{ssa-cfg-extract-flame-graph}}" to {
                FlameGraphGenerator(config, ssacfgExtract.jfr).generate()
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
