package moe.gogo

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.bool
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonParser
import java.io.File

data class Config(
    val java: String,
    val workingDir: String,
    val outputDir: String,
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
            val obj = JsonParser().parse(json.readText()).obj
            val moduleCell = obj.getAsJsonObject("module-call")
            val ssacfgExtract = obj.getAsJsonObject("ssa-cfg-extract")
            val flameGraph = obj.getAsJsonObject("flame-graph")

            return Config(
                obj["java"].string,
                obj["working-dir"].string,
                obj["output-dir"].string,
                obj["jar"].string,
                obj["vm-args"].array.map { it.string },
                obj["android-lib"].string,
                obj["apk"].string,
                moduleCell["name"].string,
                moduleCell["args"].array.map { it.string },
                ssacfgExtract["name"].string,
                ssacfgExtract["args"].array.map { it.string },
                flameGraph["enable"].bool,
                flameGraph["jfr-flame-graph"].string,
                flameGraph["jfr-flame-graph-args"].array.map { it.string },
                flameGraph["flame-graph"].string,
                flameGraph["flame-graph-args"].array.map { it.string }
            )
        }
    }
}