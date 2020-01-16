package moe.gogo

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.bool
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.lang.StringBuilder

data class Config(
    val java: String,
    val workingDir: String,
    val outputDir: String,
    val jarFile: String,
    val vmArgs: List<String>,
    val androidLib: String,
    val apk: String,
    val args: List<String>,
    val enableFlameGraph: Boolean,
    val jfrFlameGraph: String,
    val jfrFlameGraphArgs: List<String>,
    val flameGraph: String,
    val flameGraphArgs: List<String>
) {
    companion object {
        fun from(args: Array<String>, json: File): Config {
            val obj = JsonParser().parse(json.readText()).obj
            val argsMap = parseArgs(args)

            fun string(name: String, vararg prefixes: String = emptyArray()) = string(argsMap, obj, name, *prefixes)
            fun bool(name: String, vararg prefixes: String = emptyArray()) = bool(argsMap, obj, name, *prefixes)
            fun stringList(name: String, vararg prefixes: String = emptyArray()) =
                stringList(argsMap, obj, name, *prefixes)

            return Config(
                string("java"),
                string("working-dir"),
                string("output-dir"),
                string("jar"),
                stringList("vm-args"),
                string("android-lib"),
                string("apk"),
                stringList("args"),
                bool("enable", "flame-graph"),
                string("jfr-flame-graph", "flame-graph"),
                stringList("jfr-flame-graph-args", "flame-graph"),
                string("flame-graph", "flame-graph"),
                stringList("flame-graph-args", "flame-graph")
            )
        }

        private fun string(
            args: Map<String, String>,
            obj: JsonObject,
            name: String,
            vararg prefixes: String = emptyArray()
        ): String {
            val key = getKey(prefixes, name)
            if (args[key] != null) return args[key]!!
            val jsonObj = getJsonObj(obj, prefixes)
            return jsonObj[name].string
        }

        private fun bool(
            args: Map<String, String>,
            obj: JsonObject,
            name: String,
            vararg prefixes: String = emptyArray()
        ): Boolean {
            val key = getKey(prefixes, name)
            if (args[key] != null) return args[key]!!.toBoolean()
            val jsonObj = getJsonObj(obj, prefixes)
            return jsonObj[name].bool
        }

        private fun stringList(
            args: Map<String, String>,
            obj: JsonObject,
            name: String,
            vararg prefixes: String = emptyArray()
        ): List<String> {
            val key = getKey(prefixes, name)
            if (args[key] != null) return args[key]!!.split(",")
            val jsonObj = getJsonObj(obj, prefixes)
            return jsonObj[name].array.map { it.string }
        }

        private fun getJsonObj(obj: JsonObject, prefixes: Array<out String>): JsonObject {
            var jsonObj = obj
            for (prefix in prefixes) {
                jsonObj = jsonObj[prefix].obj
            }
            return jsonObj
        }

        private fun getKey(prefixes: Array<out String>, name: String): String {
            val keyBuilder = StringBuilder()
            for (prefix in prefixes) {
                keyBuilder.append(prefix)
                keyBuilder.append(".")
            }
            keyBuilder.append(name)
            return keyBuilder.toString()
        }

        private fun parseArgs(args: Array<String>): Map<String, String> {
            val regex = """--(.*?)=(.*?)""".toRegex()
            val map = mutableMapOf<String, String>()
            for (arg in args) {
                val match = regex.matchEntire(arg)
                if (match != null) {
                    val key = match.groupValues[1]
                    val value = match.groupValues[2]
                    map[key] = value
                }
            }
            return map
        }
    }
}