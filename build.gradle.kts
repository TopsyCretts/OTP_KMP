import dev.detekt.gradle.Detekt
import dev.detekt.gradle.report.ReportMergeTask
import kotlin.collections.map

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKmpLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.koin.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.detekt)
    id("org.top.package-rename")
}

val excludeTasks = listOf(
    "detektAppleMainSourceSet",
    "detektAppleTestSourceSet",
    "detektIosArm64MainSourceSet",
    "detektIosArm64TestSourceSet",
    "detektIosSimulatorArm64MainSourceSet",
    "detektIosSimulatorArm64TestSourceSet",
    "detektNativeMainSourceSet",
    "detektNativeTestSourceSet"
)

tasks.withType<Detekt>().configureEach {
    reports {
        checkstyle.required.set(true)
        html.required.set(false)
        sarif.required.set(false)
        markdown.required.set(true)
    }
}

tasks.register<ReportMergeTask>("reportAll") {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/problems.html"))
    input.from(subprojects.map {
        it.tasks.withType<Detekt>().map { task ->
            task.reports.markdown.outputLocation
        }
    })
}

packageRename {
    newPackage = "org.otp.example"
}
