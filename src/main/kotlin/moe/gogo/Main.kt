package moe.gogo

import com.beust.klaxon.Klaxon
import java.io.File

fun main() {
    val config = Klaxon()
        .converter(ConfigConverter)
        .parse<Config>(File("config.json"))!!

    val root = File(config.workingDir)
    root.resolve("module-call").mkdirs()
    root.resolve("ssa-cfg-extract").mkdirs()

    println("========== module-call ==========")
    moduleCallProcessBuilder(config).start().waitFor()

    println("========== ssa-cfg-extract ==========")
    ssacfgExtractProcessBuilder(config).start().waitFor()

}

private fun moduleCallProcessBuilder(config: Config): ProcessBuilder {
    return with(config) {
        ProcessBuilder()
            .command(
                java, *vmArgs.toTypedArray(),
                "-jar", jarFile.from(workingDir), moduleCallName,
                "-a", androidLib.from(workingDir), "-i", apk.from(workingDir), *moduleCallArgs.toTypedArray()
            )
            .directory(File(workingDir).resolve("module-call"))
            .inheritIO()
    }
}

private fun ssacfgExtractProcessBuilder(config: Config): ProcessBuilder {
    return with(config) {
        ProcessBuilder()
            .command(
                java, *vmArgs.toTypedArray(),
                "-jar", jarFile.from(workingDir), ssacfgExtractName,
                "-a", androidLib.from(workingDir), "-i", apk.from(workingDir), *ssacfgExtractArgs.toTypedArray()
            )
            .directory(File(workingDir).resolve("ssa-cfg-extract"))
            .inheritIO()
    }
}

private fun String.from(dir: String): String = File(dir).resolve(this).absolutePath