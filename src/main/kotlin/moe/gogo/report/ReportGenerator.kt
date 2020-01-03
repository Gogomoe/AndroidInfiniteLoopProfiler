package moe.gogo.report

class ReportGenerator(val result: Result) {

    val dist = result.root.resolve("report")

    private val templates: List<Pair<String, () -> String>> = with(result) {
        listOf(
            "{{package-id}}" to { ssacfgExtract.packageId },
            "{{total-time}}" to { (ssacfgExtract.costTime + moduleCall.costTime).toMillis().toString() },

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
            "{{ssa-cfg-extract-apk-class-count}}" to { ssacfgExtract.apkClassCount.toString() },
            "{{ssa-cfg-extract-app-class-count}}" to { ssacfgExtract.appClassCount.toString() },
            "{{ssa-cfg-extract-loaded-class-count}}" to { ssacfgExtract.loadedClassCount.toString() },
            "{{ssa-cfg-extract-recursion-groups-count}}" to { ssacfgExtract.recursionGroupsCount.toString() },
            "{{ssa-cfg-extract-recursion-methods-count}}" to { ssacfgExtract.recursionMethodsCount.toString() },
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

        var reportTamplate = String(getResource("Report.html").readAllBytes())
        templates.forEach { (template, result) ->
            reportTamplate = reportTamplate.replace(template, result())
        }

        val report = dist.resolve("Report.html")
        report.writeBytes(reportTamplate.toByteArray())

        with(result) {
            println("Package ID: ${ssacfgExtract.packageId}")
            println("Total Cost Time: ${(ssacfgExtract.costTime + moduleCall.costTime).toMillis()}")
            println("Loaded Class Count: ${ssacfgExtract.loadedClassCount}")
            println("Recursion Count: ${ssacfgExtract.recursionGroupsCount}")
        }

        println("Report save at ${report.absolutePath}")
    }

}
