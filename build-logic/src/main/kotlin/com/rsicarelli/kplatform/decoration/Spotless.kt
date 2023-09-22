package com.rsicarelli.kplatform.decoration

import com.diffplug.gradle.spotless.SpotlessApply
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

data class SpotlessOptions(
    val hookOnBuild: Boolean = false,
    val kotlinRules: SpotlessKotlinRules = SpotlessKotlinRules(),
    val fileRules: List<SpotlessFileRule> = listOf(SpotlessKtRule, SpotlessXmlRule),
)

interface SpotlessFileRule {

    val fileExtension: String
    val targets: List<String>
    val excludes: List<String>
}

object SpotlessKtRule : SpotlessFileRule {

    override val fileExtension: String = "kts"
    override val targets: List<String> = listOf("**/*.kts")
    override val excludes: List<String> = listOf("**/build/**/*.kts")
}

object SpotlessXmlRule : SpotlessFileRule {

    override val fileExtension: String = "xml"
    override val targets: List<String> = listOf("**/*.xml")
    override val excludes: List<String> = listOf("**/build/**/*.xml")
}

data class SpotlessKotlinRules(
    val target: String = "src/**/*.kt",
)

internal fun Project.applySpotless(spotlessConfig: SpotlessOptions) {
    val project = this

    if (spotlessConfig.hookOnBuild) {
        applySpotlessForBuildTask()
    }

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
        setupKotlin(
            config = spotlessConfig.kotlinRules,
            project = project
        )

        applyFileRules(spotlessConfig.fileRules)
    }
}

private fun Project.applySpotlessForBuildTask() {
    tasks.named("build").configure {
        dependsOn(tasks.withType<SpotlessApply>())
    }
}

private fun SpotlessExtension.setupKotlin(config: SpotlessKotlinRules, project: Project) {
    kotlin {
        target(config.target)
        ktlint().setEditorConfigPath("${project.rootDir}/.editorconfig")
    }
}

private fun SpotlessExtension.applyFileRules(fileRules: List<SpotlessFileRule>) {
    fileRules.forEach { spotlessFileRule ->
        with(spotlessFileRule) {
            format(fileExtension) {
                target(targets)
                targetExclude(excludes)
            }
        }
    }
}
