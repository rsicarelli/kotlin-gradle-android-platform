package com.rsicarelli.kplatform.decoration

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.rsicarelli.kplatform.options.SpotlessOptions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal fun Project.applySpotless(spotlessConfig: SpotlessOptions) {
    val project = this

    configureSpotlessPlugin(spotlessConfig, project)

    rootProject.subprojects {
        configureSpotlessPlugin(spotlessConfig, project)
    }
}

private fun Project.configureSpotlessPlugin(
    spotlessConfig: SpotlessOptions,
    project: Project
) {
    apply<SpotlessPlugin>()

    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            ktlint().setEditorConfigPath("${project.rootDir}/.editorconfig")
        }

        spotlessConfig.fileRules.forEach { spotlessFileRule ->
            with(spotlessFileRule) {
                format(fileExtension) {
                    target(targets)
                    targetExclude(excludes)
                }
            }
        }
    }
}

