import dev.detekt.gradle.Detekt

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

android {
    namespace = "org.otp.example"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.android.material)

    implementation(projects.composeApp)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)

    //KotlinX
    implementation(libs.kotlinx.serialization.json)

    //Decompose
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose.exp)

    // MVIKotlin
    implementation(libs.mvikotlin)

    implementation(libs.bundles.koin)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.espresso.core)
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
