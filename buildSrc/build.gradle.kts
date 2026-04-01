plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(gradleApi())
    implementation(libs.jetbrains.kotlin.stdlib)
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("packageRenamePlugin"){
            id = "org.top.package-rename"
            implementationClass = "org.top.plugin.PackageRenamePlugin"
        }
    }
}