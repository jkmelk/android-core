import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object AppConfig {
    const val compileSdk = 32
    const val minSdk = 24
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val buildToolsVersion = "30.0.3"

    const val androidTestInstrumentation = "androidx.test.runner.AndroidJUnitRunner"
    const val proguardConsumerRules = "consumer-rules.pro"
    const val dimension = "default"
}