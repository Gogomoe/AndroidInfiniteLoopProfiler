package moe.gogo.report

class HTMLReportRender(private val result: Result) : ReportRender() {

    override val template: String = readString("Report.html")

    override val data: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{package-id}}" to { packageId },
            "{{total-time}}" to { costTime.toMillis().toString() },

            "{{time}}" to { costTime.toMillis().toString() },
            "{{max-heap}}" to { maxHeap.toString() },
            "{{apk-class-count}}" to { apkClassCount.toString() },
            "{{app-class-count}}" to { appClassCount.toString() },
            "{{loaded-class-count}}" to { loadedClassCount.toString() },
            "{{recursion-groups-count}}" to { recursionGroupsCount.toString() },
            "{{recursion-methods-count}}" to { recursionMethodsCount.toString() },
            "{{module-count}}" to { moduleCount.toString() },
            "{{module-transition-count}}" to { moduleTransitionCount.toString() },
            "{{module-recursion-groups-count}}" to { moduleRecursionGroupsCount.toString() },
            "{{module-recursion-modules-count}}" to { moduleRecursionModuleCount.toString() },
            "{{method-call-dot-graph-js}}" to {
                DotGraphRender("method-call-dot-graph", methodCallDot.readText()).render()
            },
            "{{module-call-dot-graph-js}}" to {
                DotGraphRender("module-call-dot-graph", moduleCallDot.readText()).render()
            },
            "{{memory-chart-js}}" to {
                MemoryChartRender("ssa-cfg-extract-memory", memory).render()
            },
            "{{flame-graph}}" to {
                FlameGraphRender(config, jfr).generate()
            }
        )
    }

}