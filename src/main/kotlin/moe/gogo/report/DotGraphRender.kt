package moe.gogo.report

class DotGraphRender(private val id: String, private val dot: String) : ReportRender() {

    override val template: String = readString("dot-graph-template.js")

    override val data: List<Pair<String, () -> String>> = listOf(
        "{{dot-graph-id}}" to { id },
        "{{dot-graph}}" to { dot }
    )

}