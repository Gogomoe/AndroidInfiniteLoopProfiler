package moe.gogo

import moe.gogo.jfr.MemoryRecordReader
import moe.gogo.report.ReportGenerator
import moe.gogo.report.Result
import moe.gogo.report.SSACFGExtractResult
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    Main().run()
}

class Main {

    private val config: Config = Config.from(File("config.json"))
    private val root: File = File(config.workingDir)
    private val output: File = outputDir(root, config.outputDir)
    private val ssacfgExtractDir = output.resolve("ssa-cfg-extract").also { it.mkdirs() }

    fun run() {

        println("========== ssa-cfg-extract ==========")
        val ssacfgExtractProcess = ssacfgExtractProcessBuilder()
        println(ssacfgExtractProcess.command())
        val ssacfgExtractTime = calcTime {
            ssacfgExtractProcess.start().waitFor()
        }

        println("========== summary ==========")
        val result = Result(
            config,
            root,
            output,
            SSACFGExtractResult(
                ssacfgExtractDir,
                ssacfgExtractTime,
                MemoryRecordReader(ssacfgExtractDir.resolve("recording.jfr")).load()
            )
        )
        ReportGenerator(result).generate()
    }

    private fun outputDir(root: File, outputDir: String): File {
        val regex = """\$\{(.*)\}""".toRegex()
        val result = regex.find(outputDir) ?: return root.resolve(outputDir)
        val pattern = result.groupValues[1]

        val format = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now())
        val dirname = regex.replaceFirst(outputDir, format)

        return root.resolve(dirname)
    }

    private fun ssacfgExtractProcessBuilder(): ProcessBuilder {
        return with(config) {
            ProcessBuilder()
                .command(
                    java, *vmArgs.toTypedArray(),
                    "-jar", rootTo(jarFile), ssacfgExtractName,
                    "-a", rootTo(androidLib), "-i", rootTo(apk),
                    *ssacfgExtractArgs.toTypedArray()
                )
                .directory(ssacfgExtractDir)
                .inheritIO()
        }
    }

    private fun calcTime(block: () -> Unit): Duration {
        val start = Instant.now()
        block()
        val end = Instant.now()
        return Duration.between(start, end)
    }

    private fun rootTo(path: String) = root.resolve(path).absolutePath

}