package org.top.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class PackageRenameExtension @Inject constructor(objects: ObjectFactory){
    val newPackage: Property<String> = objects.property(String::class.java)
}