package com.rsicarelli.kplatform.decoration

import com.rsicarelli.kplatform.options.DetektOptions
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal fun Project.applyDetekt(
    detektOptions: DetektOptions,
) {
    check(rootProject == this) { "Must be called on a root project" }

    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        parallel = detektOptions.parallel
        toolVersion = libs.version("detekt")
        source.setFrom("$projectDir/${detektOptions.configFileName}")
    }

    tasks.withType<Detekt> {
        setSource(files(projectDir))
        include(detektOptions.includes)
        exclude(detektOptions.excludes)
    }
}
