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

internal fun Project.applyDetekt(
    detektOptions: DetektOptions,
) {
    check(rootProject == this) { "Must be called on a root project" }

    val klintVersion = libs.version("detekt")
    val dependency = libs.findLibrary("detektRules-compose").get().get()
    val dependency1 = libs.findLibrary("detektRules-formatting").get().get()
    val dependency2 = libs.findLibrary("detektRules-libraries").get().get()

    subprojects {
        pluginManager.apply("io.gitlab.arturbosch.detekt")
        extensions.configure<DetektExtension> {
            parallel = detektOptions.parallel
            toolVersion = klintVersion
            buildUponDefaultConfig = detektOptions.buildUponDefaultConfig
            config.setFrom(detektOptions.configFileNames.map { "$rootDir/${it}" })
        }

        tasks.withType<Detekt> {
            setSource(files(projectDir))
            include(detektOptions.includes)
            exclude(detektOptions.excludes)
        }

        dependencies {

            detektPlugins(dependency)
            detektPlugins(dependency1)
            detektPlugins(dependency2)
        }
    }
}

fun DependencyHandlerScope.detektPlugins(dependency: MinimalExternalModuleDependency) {
    add("detektPlugins", dependency)
}
