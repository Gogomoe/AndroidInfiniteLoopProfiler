package moe.gogo.report

import moe.gogo.Config
import java.io.File

class FlameGraphRender(private val config: Config, private val jfr: File) : ReportRender() {

    override val template: String by lazy { generate() }

    fun generate(): String {
        if (!config.enableFlameGraph) {
            return ""
        }
        val dir = jfr.parentFile
        val jfrFlameGraphFile = dir.resolve("jfr-flame-graph.txt")
        val flameGraphFile = dir.resolve("flame-graph.svg")

        jfrFlameGraph(jfrFlameGraphFile, dir).start().waitFor()
        flameGraphFile(dir, jfrFlameGraphFile, flameGraphFile).start().waitFor()

        return extractSVGElement(flameGraphFile)
    }

    private fun jfrFlameGraph(jfrFlameGraphFile: File, dir: File?): ProcessBuilder {
        return with(config) {
            ProcessBuilder()
                .command(
                    jfrFlameGraph,
                    "-f", jfr.absolutePath,
                    "-o", jfrFlameGraphFile.absolutePath,
                    *jfrFlameGraphArgs.toTypedArray()
                )
                .directory(dir)
                .inheritIO()
        }
    }

    private fun flameGraphFile(dir: File?, jfrFlameGraphFile: File, flameGraphFile: File): ProcessBuilder {
        return with(config) {
            ProcessBuilder()
                .command(
                    flameGraph,
                    *flameGraphArgs.toTypedArray()
                )
                .directory(dir)
                .redirectInput(jfrFlameGraphFile)
                .redirectOutput(flameGraphFile)
        }
    }

    private fun extractSVGElement(flameGraphFile: File): String {
        val flameGraphStr = flameGraphFile.readText()
        val svgStart = flameGraphStr.indexOf("<svg")
        val svgEnd = flameGraphStr.lastIndexOf("</svg>") + 6
        check(svgStart >= 0 && svgEnd >= 0 && svgEnd >= svgStart) { "error svg position" }
        return flameGraphStr.substring(svgStart, svgEnd)
    }

}