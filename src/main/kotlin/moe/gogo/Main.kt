package moe.gogo

import com.beust.klaxon.Klaxon
import java.io.File

fun main() {
//    val record = MemoryRecordReader(File("recording.jfr")).load()
//    ReportGenerator(File("report"), record).generate()
    val config = Klaxon().parse<Config>(File("config.json"))!!

    val root = File(config.workingDir)
    root.resolve("module-call").mkdirs()
    root.resolve("ssa-cfg-extract").mkdirs()

    moduleCallProcessBuilder(config).start().waitFor()
    ssacfgExtractProcessBuilder(config).start().waitFor()

}

private fun moduleCallProcessBuilder(config: Config): ProcessBuilder {
    return with(config) {
        ProcessBuilder(java)
            .directory(File(workingDir).resolve("module-call"))
            .command(vmArgs)
            .command("-jar", jarFile.from(workingDir), moduleCallName)
            .command("-a", androidLib.from(workingDir), "-i", apk.from(workingDir), *moduleCallArgs.toTypedArray())
            .inheritIO()
    }
}

private fun ssacfgExtractProcessBuilder(config: Config): ProcessBuilder {
    return with(config) {
        ProcessBuilder(java)
            .directory(File(workingDir))
            .command(vmArgs)
            .command("-jar", jarFile.from(workingDir), ssacfgExtractName)
            .command("-a", androidLib.from(workingDir), "-i", apk.from(workingDir), *ssacfgExtractArgs.toTypedArray())
            .inheritIO()
    }
}

private fun String.from(dir: String): String = File(dir).resolve(this).absolutePath