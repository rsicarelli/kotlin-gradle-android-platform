package com.rsicarelli.kplatform.decoration

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.rsicarelli.kplatform.options.SpotlessOptions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Applies the Spotless code formatting and linting plugin to the project and its subprojects.
 *
 * This function configures the Spotless plugin based on the provided [spotlessConfig]. It first applies the
 * Spotless configuration to the current project and then iterates over all the subprojects of the root project
 * to apply the same configuration.
 *
 * @param spotlessConfig The configuration options to use for the Spotless plugin.
 * @receiver [Project] The Gradle project on which the function is applied.
 */
internal fun Project.applySpotless(spotlessConfig: SpotlessOptions) {
    val project = this

    configureSpotlessPlugin(spotlessConfig, project)

    rootProject.subprojects {
        configureSpotlessPlugin(spotlessConfig, project)
    }
}

/**
 * Configures the Spotless plugin for a given project.
 *
 * This function configures the Spotless plugin with the provided [spotlessConfig]. It sets up rules for Kotlin
 * files and other file formats as specified in the configuration.
 *
 * @param spotlessConfig The configuration options to use for the Spotless plugin.
 * @param project The Gradle project to which the Spotless plugin configuration should be applied.
 * @receiver [Project] The Gradle project on which the function is applied. (In most cases, this will be the root project.)
 */
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

