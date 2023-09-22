package com.rsicarelli.kplatform.decoration

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Provides access to the version catalog named "libs".
 *
 * Due to composite builds, there's no direct access to the generated libs accessor.
 * Thus, this extension property provides a way to access the named version catalog "libs".
 *
 * @receiver [Project] The Gradle project on which the property is accessed.
 * @return The version catalog named "libs".
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Retrieves the version of a given library or module from the version catalog.
 *
 * This function simplifies accessing specific versions from a version catalog by
 * abstracting the find operation.
 *
 * @param name The name of the library or module for which the version is sought.
 * @receiver [VersionCatalog] The version catalog from which the version is retrieved.
 * @return The version string associated with the provided name.
 */
internal fun VersionCatalog.version(name: String): String = findVersion(name).get().toString()
