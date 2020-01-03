package moe.gogo.report

class HTMLReportRender(private val result: Result) : ReportRender() {

    override val template: String = readString("Report.html")

    override val data: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{package-id}}" to { ssacfgExtract.packageId },
            "{{total-time}}" to { ssacfgExtract.costTime.toMillis().toString() },

            "{{time}}" to { ssacfgExtract.costTime.toMillis().toString() },
            "{{max-heap}}" to { ssacfgExtract.maxHeap.toString() },
            "{{apk-class-count}}" to { ssacfgExtract.apkClassCount.toString() },
            "{{app-class-count}}" to { ssacfgExtract.appClassCount.toString() },
            "{{loaded-class-count}}" to { ssacfgExtract.loadedClassCount.toString() },
            "{{recursion-groups-count}}" to { ssacfgExtract.recursionGroupsCount.toString() },
            "{{recursion-methods-count}}" to { ssacfgExtract.recursionMethodsCount.toString() },
            "{{module-count}}" to { ssacfgExtract.moduleCount.toString() },
            "{{module-transition-count}}" to { ssacfgExtract.moduleTransitionCount.toString() },
            "{{module-recursion-groups-count}}" to { ssacfgExtract.moduleRecursionGroupsCount.toString() },
            "{{module-recursion-modules-count}}" to { ssacfgExtract.moduleRecursionModuleCount.toString() },
            "{{method-call-dot-graph-js}}" to {
                DotGraphRender("method-call-dot-graph", ssacfgExtract.methodCallDot.readText()).render()
            },
            "{{module-call-dot-graph-js}}" to {
                DotGraphRender("module-call-dot-graph", ssacfgExtract.moduleCallDot.readText()).render()
            },
            "{{memory-chart-js}}" to {
                MemoryChartRender("ssa-cfg-extract-memory", ssacfgExtract.memory).render()
            },
            "{{flame-graph}}" to {
                FlameGraphRender(config, ssacfgExtract.jfr).generate()
            }
        )
    }

}