package moe.gogo.report

import moe.gogo.Config
import java.io.File

class FlameGraphGenerator(val config: Config, val jfr: File) {

    fun generate(): String {
        if (!config.enableFlameGraph) {
            return ""
        }
        val dir = jfr.parentFile
        val jfrFlameGraphFile = dir.resolve("jfr-flame-graph.txt")
        val jfrProcess = with(config) {
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
        jfrProcess.start().waitFor()

        val flameGraphFile = dir.resolve("flame-graph.svg")
        val flameProcess = with(config) {
            ProcessBuilder()
                .command(
                    flameGraph,
                    *flameGraphArgs.toTypedArray()
                )
                .directory(dir)
                .redirectInput(jfrFlameGraphFile)
                .redirectOutput(flameGraphFile)
        }
        flameProcess.start().waitFor()

        val flameGraphStr = flameGraphFile.readText()
        val svgStart = flameGraphStr.indexOf("<svg")
        val svgEnd = flameGraphStr.lastIndexOf("</svg>") + 6
        check(svgStart >= 0 && svgEnd >= 0 && svgEnd >= svgStart) { "error svg position" }

        return flameGraphStr.substring(svgStart, svgEnd)
    }

}