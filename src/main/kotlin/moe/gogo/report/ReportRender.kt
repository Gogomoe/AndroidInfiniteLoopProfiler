package moe.gogo.report

open class ReportRender {

    protected open val template: String = ""

    protected open val data: List<Pair<String, () -> String>> = listOf()

    open fun render(): String {
        var result = template
        data.forEach { (match, value) ->
            result = result.replace(match, value())
        }
        return result
    }

}