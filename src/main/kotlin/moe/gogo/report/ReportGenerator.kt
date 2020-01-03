package moe.gogo.report

class ReportGenerator(val result: Result) {

    val dist = result.output

    fun generate() {
        if (!dist.exists()) {
            dist.mkdir()
        }

        val render = HTMLReportRender(result)

        val report = dist.resolve("Report.html")
        report.writeBytes(render.render().toByteArray())

        with(result) {
            println("Package ID: ${ssacfgExtract.packageId}")
            println("Total Cost Time: ${ssacfgExtract.costTime.toMillis()}")
            println("Loaded Class Count: ${ssacfgExtract.loadedClassCount}")
            println("Recursion Count: ${ssacfgExtract.recursionGroupsCount}")
        }

        println("Report save at ${report.absolutePath}")
    }

}
