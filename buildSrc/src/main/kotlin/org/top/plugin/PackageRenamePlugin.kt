package org.top.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import kotlin.io.path.Path
import kotlin.io.path.pathString

class PackageRenamePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions
            .create("packageRename", PackageRenameExtension::class.java)

        val renameTasks = project.subprojects.mapNotNull { sub ->
            val buildFile = sub.buildFile
            if (buildFile.exists()){
                val taskName = "renamePackageIn${sub.name.capitalized()}"
                sub.tasks.register(taskName, RenamePackageTask::class.java) {
                    group = "package"
                    description = "Rename ${sub.name} src root package to ${extension.newPackage.get()}"
                    newPackageBase.set(extension.newPackage.get())
                    val path = Path(project.rootDir.path, sub.path)
                    projectPath.set(path.pathString)
                }
            } else null
        }

        project.tasks
            .register("renameAllPackages") {
                group = "package"
                description = "Rename src root package in all subprojects"
                dependsOn(renameTasks)
            }
    }
}