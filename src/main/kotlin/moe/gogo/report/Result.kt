package moe.gogo.report

import com.github.salomonbrys.kotson.int
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonParser
import moe.gogo.Config
import moe.gogo.jfr.MemoryRecord
import java.io.File
import java.time.Duration

data class Result(
    val config: Config,
    val root: File,
    val output: File,
    val costTime: Duration,
    val memory: MemoryRecord
) {
    val maxHeap: Long = memory.record.map { it.used }.max()!!
    val jfr: File = output.resolve("recording.jfr")
    val methodCallDot: File = output.resolve("ssaOutput").resolve("call-graph.dot")
    val moduleCallDot: File = output.resolve("ssaOutput").resolve("module-cg.dot")

    val packageId: String
    val apkClassCount: Int
    val appClassCount: Int
    val loadedClassCount: Int
    val recursionGroupsCount: Int
    val recursionMethodsCount: Int
    val moduleCount: Int
    val moduleTransitionCount: Int
    val moduleRecursionGroupsCount: Int
    val moduleRecursionModuleCount: Int

    val loopCFG: List<File> = output.resolve("ssaOutput").resolve("loop_cfgs").listFiles { file ->
        file.name.endsWith(".dot")
    }!!.toList()

    init {
        val stat: File = output.resolve("stat.json")
        val obj = JsonParser().parse(stat.readText()).obj
        packageId = obj["packageId"].string
        apkClassCount = obj["apkClassCount"].int
        appClassCount = obj["appClassCount"].int
        loadedClassCount = obj["loadedClassCount"].int
        recursionGroupsCount = obj["recursionGroupsCount"].int
        recursionMethodsCount = obj["recursionMethodsCount"].int
        moduleCount = obj["moduleCount"].int
        moduleTransitionCount = obj["transitionCount"].int
        moduleRecursionGroupsCount = obj["moduleRecursionGroupsCount"].int
        moduleRecursionModuleCount = obj["moduleRecursionModuleCount"].int
    }
}
