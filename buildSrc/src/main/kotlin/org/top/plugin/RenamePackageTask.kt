package org.top.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RenamePackageTask : DefaultTask() {

    @get:Input
    abstract val newPackageBase: Property<String>

    @get:Input
    abstract val projectPath: Property<String>

    @TaskAction
    fun rename() {
        val newBasePackage = newPackageBase.get()
        val path = projectPath.get().replace(":", "/")
        val projectDir = File(path).absoluteFile
        if (!projectDir.exists()) {
            throw IllegalStateException("Project directory $path does not exist")
        }

        val buildFile = findBuildFile(projectDir)
        val oldPackageName = resolveOldPackageNames(buildFile).replace(".", "/")
        logger.lifecycle("Old package name: $oldPackageName")
        val newPackageName = renameOldBasePackage(oldPackageName, newBasePackage)
        logger.lifecycle("New package name: $newPackageName")

        if (oldPackageName == newPackageName) {
            logger.lifecycle("No need to rename package")
            return
        }

        val srcDirs = getSourceDirectories(path)
        for (dir in srcDirs) {
            renamePackageInSrcSubDir(
                dir,
                oldPackageName,
                newPackageName
            )
        }
        logger.lifecycle("Replacing in build.gradle.kts...")
        replacePackageInFile(buildFile, oldPackageName, newPackageName)
        logger.lifecycle("Renamed package in $path")
    }

    private fun findBuildFile(projectDir: File): File {
        val buildFile = projectDir.resolve("build.gradle.kts")
        if (!buildFile.exists()) {
            throw IllegalStateException("build.gradle.kts does not exist in $projectDir")
        }
        return buildFile
    }

    private fun resolveOldPackageNames(buildFile: File): String {
        val content = buildFile.readText()
        val regex = Regex("namespace\\s*=\\s*\"([^\"]*)\"")
        val matchResult = regex.find(content)
            ?: throw IllegalStateException("Could not find namespace in $buildFile")

        return matchResult.groupValues[1]
    }

    private fun renameOldBasePackage(oldPackage: String, newBasePackage: String): String {
        val oldPackageParts = oldPackage.split("/")
        val newPackageParts = newBasePackage.split(".")
        val newPackage = newPackageParts + oldPackageParts.subList(3, oldPackageParts.size)
        return newPackage.joinToString("/")
    }

    private fun getSourceDirectories(path: String): List<File> {
        val dir = File(path)
        if (!dir.exists()) {
            logger.warn("Directory $path does not exist")
            return emptyList()
        }
        val src = dir.resolve("src")
        if (!src.exists()) {
            logger.warn("Directory $src does not exist")
            return emptyList()
        }
        val sourceDirectories = mutableListOf<File>()
        for (file in src.listFiles()) {
            if (file.isDirectory) {
                sourceDirectories.add(file)
            }
        }
        return sourceDirectories
    }

    private fun renamePackageInSrcSubDir(srcDir: File, oldPackage: String, newPackage: String) {
        for (file in srcDir.listFiles()) {
            if (!file.isDirectory) {
                continue
            }
            if (file.name !in listOf("java", "kotlin", "groovy")) {
                continue
            }
            logger.lifecycle("Renaming package in: ${file.path}")
            logger.lifecycle("")
            copyFiles(file, oldPackage, newPackage)
            logger.lifecycle("Files copied \n")

            logger.lifecycle("Replacing in files...")
            replacePackageInDir(file, oldPackage, newPackage)
            logger.lifecycle("Files replaced \n")
            logger.lifecycle("---------------------------------------------------------------")
        }
    }

    private fun copyFiles(baseDir: File, oldPath: String, newPath: String) {
        val newDir = File(baseDir, newPath).absoluteFile
        logger.lifecycle("New package: $newDir")

        val oldPackage = baseDir.resolve(oldPath)
        logger.lifecycle("Old package: $oldPackage")

        if (!oldPackage.exists()) {
            logger.warn("Old package does not exist: $oldPackage")
            return
        }

        if (!newDir.exists()) {
            logger.lifecycle("Creating new package: $newDir")
            if (!newDir.mkdirs()) {
                logger.warn("Could not create new package: $newDir")
                return
            }
        }

        logger.lifecycle("RenameTo: $oldPackage -> $newDir")
        oldPackage.copyRecursively(newDir, true)
        logger.lifecycle("Cleaning empty dirs...")
        val oldBase = resolveOldDirBaseChange(oldPackage.absolutePath, newDir.absolutePath)
        cleanOldDirs(oldBase, oldPackage)
    }

    private fun resolveOldDirBaseChange(oldPackage: String, newPackage: String): File {
        val oldPackageParts = oldPackage.split("/")
        val newPackageParts = newPackage.split("/")
        var changedIndex = oldPackageParts.size -1
        for (i in oldPackageParts.size-1 downTo 0) {
            if (oldPackageParts[i] != newPackageParts[i]) {
                changedIndex = i
                break
            }
        }
        val base = oldPackageParts.subList(0, changedIndex).joinToString("/")
        logger.lifecycle("Old base: $base")
        return File(base)
    }

    private fun cleanOldDirs(base: File, oldPackage: File) {
        var current = oldPackage
        while (current != base) {
            if (!current.exists()) {
                current = current.parentFile
                continue
            }
            val children = current.listFiles() ?: arrayOf()
            children.forEach {
                if (it.isDirectory){
                    it.deleteRecursively()
                } else it.delete()
            }
            if (current.delete()) {
                logger.lifecycle("Deleted empty directory: $current")
                current = current.parentFile
            } else {
                logger.lifecycle("Could not delete dir: $current")
                break
            }
        }
    }

    private fun replacePackageInDir(dir: File, oldPackage: String, newPackage: String) {
        val oldBase = oldPackage.split("/").subList(0, 3).joinToString("/")
        val newBase = newPackage.split("/").subList(0,3).joinToString("/")
        for (file in dir.listFiles()) {
            if (file.isDirectory) {
                logger.lifecycle("Renaming package in: ${file.path}")
                replacePackageInDir(file, oldBase, newBase)
            }
            if (file.extension == "kt") {
                replacePackageInFile(file, oldBase, newBase)
            }
        }
    }

    private fun replacePackageInFile(file: File, oldPackage: String, newPackage: String) {
        val old = oldPackage.replace("/", ".")
        val new = newPackage.replace("/", ".")
        val content = file.readText()
        logger.lifecycle("Replacing $old with $new in ${file.path}")
        val newContent = content.replace(old, new)
        file.writeText(newContent)
    }

}