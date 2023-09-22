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

    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        parallel = detektOptions.parallel
        toolVersion = libs.version("detekt")
        buildUponDefaultConfig = detektOptions.buildUponDefaultConfig
        config.setFrom(detektOptions.configFileNames.map { "$projectDir/${it}" })
    }

    tasks.withType<Detekt> {
        setSource(files(projectDir))
        include(detektOptions.includes)
        exclude(detektOptions.excludes)
    }

    dependencies {
        detektPlugins(libs.findLibrary("detektRules-compose").get().get())
        detektPlugins(libs.findLibrary("detektRules-formatting").get().get())
        detektPlugins(libs.findLibrary("detektRules-libraries").get().get())
    }
}

fun DependencyHandlerScope.detektPlugins(dependency: MinimalExternalModuleDependency) {
    add("detektPlugins", dependency)
}
