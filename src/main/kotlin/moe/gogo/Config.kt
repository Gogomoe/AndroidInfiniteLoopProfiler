package moe.gogo

import com.google.gson.JsonParser
import java.io.File

data class Config(
    val java: String,
    val workingDir: String,
    val jarFile: String,
    val vmArgs: List<String>,
    val androidLib: String,
    val apk: String,
    val moduleCallName: String,
    val moduleCallArgs: List<String>,
    val ssacfgExtractName: String,
    val ssacfgExtractArgs: List<String>,
    val enableFlameGraph: Boolean,
    val jfrFlameGraph: String,
    val jfrFlameGraphArgs: List<String>,
    val flameGraph: String,
    val flameGraphArgs: List<String>
) {
    companion object {
        fun from(json: File): Config {
            val obj = JsonParser.parseString(json.readText()).asJsonObject
            val moduleCell = obj.getAsJsonObject("module-call")
            val ssacfgExtract = obj.getAsJsonObject("ssa-cfg-extract")
            val flameGraph = obj.getAsJsonObject("flame-graph")

            return Config(
                obj.getAsJsonPrimitive("java").asString,
                obj.getAsJsonPrimitive("working-directory").asString,
                obj.getAsJsonPrimitive("jar").asString,
                obj.getAsJsonArray("vm-args").map { it.asString },
                obj.getAsJsonPrimitive("android-lib").asString,
                obj.getAsJsonPrimitive("apk").asString,
                moduleCell.getAsJsonPrimitive("name").asString,
                moduleCell.getAsJsonArray("args").map { it.asString },
                ssacfgExtract.getAsJsonPrimitive("name").asString,
                ssacfgExtract.getAsJsonArray("args").map { it.asString },
                flameGraph.getAsJsonPrimitive("enable").asBoolean,
                flameGraph.getAsJsonPrimitive("jfr-flame-graph").asString,
                flameGraph.getAsJsonArray("jfr-flame-graph-args").map { it.asString },
                flameGraph.getAsJsonPrimitive("flame-graph").asString,
                flameGraph.getAsJsonArray("flame-graph-args").map { it.asString }
            )
        }
    }
}