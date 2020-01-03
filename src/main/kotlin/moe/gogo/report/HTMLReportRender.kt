package moe.gogo.report

class HTMLReportRender(private val result: Result) : ReportRender() {

    override val template: String = readString("Report.html")

    override val data: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{package-id}}" to { ssacfgExtract.packageId },
            "{{total-time}}" to { (ssacfgExtract.costTime + moduleCall.costTime).toMillis().toString() },

            "{{module-call-time}}" to { moduleCall.costTime.toMillis().toString() },
            "{{module-call-max-heap}}" to { moduleCall.maxHeap.toString() },
            "{{module-call-dot-graph-js}}" to {
                DotGraphRender("module-call-dot-graph", moduleCall.dot.readText()).render()
            },
            "{{module-call-memory-chart-js}}" to {
                MemoryChartRender("module-call-memory", moduleCall.memory).render()
            },
            "{{module-call-flame-graph}}" to {
                FlameGraphRender(config, moduleCall.jfr).generate()
            },

            "{{ssa-cfg-extract-time}}" to { ssacfgExtract.costTime.toMillis().toString() },
            "{{ssa-cfg-extract-max-heap}}" to { ssacfgExtract.maxHeap.toString() },
            "{{ssa-cfg-extract-apk-class-count}}" to { ssacfgExtract.apkClassCount.toString() },
            "{{ssa-cfg-extract-app-class-count}}" to { ssacfgExtract.appClassCount.toString() },
            "{{ssa-cfg-extract-loaded-class-count}}" to { ssacfgExtract.loadedClassCount.toString() },
            "{{ssa-cfg-extract-recursion-groups-count}}" to { ssacfgExtract.recursionGroupsCount.toString() },
            "{{ssa-cfg-extract-recursion-methods-count}}" to { ssacfgExtract.recursionMethodsCount.toString() },
            "{{ssa-cfg-extract-graph-js}}" to {
                DotGraphRender("ssa-cfg-extract-dot-graph", ssacfgExtract.dot.readText()).render()
            },
            "{{ssa-cfg-extract-memory-chart-js}}" to {
                MemoryChartRender("ssa-cfg-extract-memory", ssacfgExtract.memory).render()
            },
            "{{ssa-cfg-extract-flame-graph}}" to {
                FlameGraphRender(config, ssacfgExtract.jfr).generate()
            }
        )
    }

}