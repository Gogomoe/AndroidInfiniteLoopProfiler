package moe.gogo.report

class DotGraphRender(val id: String, val dot: String) {

    private val templates: List<Pair<String, () -> String>> = listOf(
        "{{dot-graph-id}}" to { id },
        "{{dot-graph}}" to { dot }
    )

    fun render(): String {
        var graphJs = String(getResource("dot-graph-template.js").readAllBytes())
        templates.forEach { (template, result) ->
            graphJs = graphJs.replace(template, result())
        }

        return graphJs
    }
}