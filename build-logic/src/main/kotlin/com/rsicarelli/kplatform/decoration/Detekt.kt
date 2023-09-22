package com.rsicarelli.kplatform.decoration

import com.rsicarelli.kplatform.options.DetektOptions
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Configures the Detekt linting tool for the given project using the provided options.
 *
 * Note: This function must be called only on the root project.
 *
 * @param detektOptions Configuration options for the Detekt tool.
 * @throws IllegalStateException if the function is not called on the root project.
 *
 * @see DetektOptions
 * @see DetektExtension
 */
internal fun Project.applyDetekt(
    detektOptions: DetektOptions
) {
    check(rootProject == this) { "Must be called on a root project" }

    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        parallel = detektOptions.parallel
        toolVersion = libs.version("detekt")
        buildUponDefaultConfig = detektOptions.buildUponDefaultConfig
        config.setFrom(detektOptions.configFileNames.map { "$rootDir/$it" })
    }

    tasks.withType<Detekt> {
        setSource(files(projectDir))
        include(detektOptions.includes)
        exclude(detektOptions.excludes)
    }

    addDetektPlugins(listOf("compose", "formatting", "libraries"))
}

/**
 * Adds the specified Detekt plugins to the project.
 *
 * This function facilitates the inclusion of Detekt plugins by appending the prefix `detektRules-`
 * to each provided plugin name, resolving them via the `libs` handler, and then adding them to the
 * `detektPlugins` dependency configuration.
 *
 * NOTE: it should match with Version catalog library declaration
 *
 * @param detektPlugins List of Detekt plugin names (without the `detektRules-` prefix).
 *                      For instance, if you have a library named "detektRules-compose",
 *                      you'd simply pass in "compose".
 *
 * @receiver [Project] The Gradle project on which the function is applied.
 */
fun Project.addDetektPlugins(detektPlugins: List<String>) {
    fun DependencyHandlerScope.detektPlugin(dependency: MinimalExternalModuleDependency) {
        add("detektPlugins", dependency)
    }

    dependencies {
        detektPlugins.forEach { plugin ->
            detektPlugin(libs.findLibrary("detektRules-$plugin").get().get())
        }
    }
}
