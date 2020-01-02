package moe.gogo.report

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

internal fun getResource(filename: String) = ReportGenerator::class.java.classLoader.getResourceAsStream(filename)!!

internal fun copy(filename: String, dist: File) {
    Files.copy(getResource(filename), dist.toPath().resolve(filename), StandardCopyOption.REPLACE_EXISTING)
}