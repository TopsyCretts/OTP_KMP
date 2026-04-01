import dev.detekt.gradle.Detekt

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.detekt)
}

kotlin {

    android {
        namespace = "org.otp.example.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.bundles.decompose)
            export(libs.bundles.mvikotlin)
            export(libs.bundles.essenty)
            export(libs.bundles.koin)
        }
    }

    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)

            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.compose.navigationevent)

            //KotlinX
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.bundles.ktor.common)
            api(libs.bundles.koin)
            api(libs.bundles.decompose)
            api(libs.bundles.mvikotlin)
            api(libs.bundles.essenty)

            api(projects.modules.core)
            implementation(projects.modules.main)
        }
        iosMain.dependencies {

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

detekt {
    ignoreFailures = true

    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))

}

tasks.withType<Detekt>().configureEach {
    reports {
        checkstyle.required.set(true)
        html.required.set(false)
        sarif.required.set(false)
        markdown.required.set(true)
    }
}
