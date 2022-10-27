import org.gradle.api.Project
import java.io.FileInputStream
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

private fun Project.getMatcher(): Matcher {
    val gradle = gradle
    val tskReqStr = gradle.startParameter.taskRequests.toString()
    println(tskReqStr)
    val pattern = if (tskReqStr.contains("assemble")) {
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    } else {
        Pattern.compile("generate(\\w+)(Release|Debug)")
    }

    return pattern.matcher(tskReqStr)
}

fun Project.getCurrentBuildType(): String {
    val matcher = getMatcher()
    return if (matcher.find()) {
        matcher.group(2).toLowerCase()
    } else {
        ""
    }
}

fun Project.getCurrentFlavor(): String {
    val matcher = getMatcher()
    return if (matcher.find()) {
        matcher.group(1).toLowerCase()
    } else {
        ""
    }
}

fun Project.readVersionCode(): Int {
    val versionProps = readProperty("version.properties", "VERSION_CODE")
    val currentCode = Integer.parseInt(versionProps)
    return currentCode
}

fun Project.incrementVersionCode() {
    var currentCode = readVersionCode()
    if (getCurrentBuildType() == "release") {
        val versionPropsFile = file("version.properties")
        val versionProps = Properties()
        currentCode += 1
        versionProps["VERSION_CODE"] = currentCode.toString()
        versionProps.store(versionPropsFile.outputStream(), null)
    }
}

fun Project.buildVersionName(): String {
    val version = readVersionCode().toString()
    return when (version.length) {
        1 -> "1.0.${version[0]}"
        2 -> "1.${version[0]}.${version[1]}"
        3 -> "${version[0]}.${version[1]}.${version[2]}"
        else -> "0.0.1"
    }
}

fun Project.readProperty(file: String, propertyName: String): String {
    val versionPropsFile = file(file)
    val versionProps = Properties()
    versionProps.load(FileInputStream(versionPropsFile))
    return versionProps[propertyName].toString()
}

fun Project.readFile(file: String): Properties {
    val versionPropsFile = file(file)
    val versionProps = Properties()
    versionProps.load(FileInputStream(versionPropsFile))
    return versionProps
}