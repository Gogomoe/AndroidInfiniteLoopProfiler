package moe.gogo

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

val ConfigConverter = object : Converter {

    override fun canConvert(cls: Class<*>) = cls == Config::class.java

    override fun toJson(value: Any): String = """{"message": "cannot convert to JSON"}"""

    override fun fromJson(jv: JsonValue): Config {
        val obj = jv.obj!!
        val moduleCell = obj.obj("module-call")!!
        val ssacfgExtract = obj.obj("ssa-cfg-extract")!!
        val flameGraph = obj.obj("flame-graph")!!

        return Config(
            obj.string("java")!!,
            obj.string("working-directory")!!,
            obj.string("jar")!!,
            obj.array<String>("vm-args")!!.toList(),
            obj.string("android-lib")!!,
            obj.string("apk")!!,
            moduleCell.string("name")!!,
            moduleCell.array<String>("args")!!.toList(),
            ssacfgExtract.string("name")!!,
            ssacfgExtract.array<String>("args")!!.toList(),
            flameGraph.boolean("enable")!!,
            flameGraph.string("jfr-flame-graph")!!,
            flameGraph.array<String>("jfr-flame-graph-args")!!.toList(),
            flameGraph.string("flame-graph")!!,
            flameGraph.array<String>("flame-graph-args")!!.toList()
        )
    }

}