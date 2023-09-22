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
    val kotlinFileRules: SpotlessKotlinFileRules = SpotlessKotlinFileRules(),
    val xmlRules: SpotlessXmlRules = SpotlessXmlRules()
)

data class SpotlessKotlinRules(
    val targets: List<String> = listOf("**/*.kt"),
    val excludes: List<String> = listOf("**/build/**/*.kt", "**/test/**", "**/androidTest/**", "**/*.Test.kt"),
    val userData: HashMap<String, String> = hashMapOf("android" to "true")
)

data class SpotlessKotlinFileRules(
    val fileExtension: String = "kts",
    val targets: List<String> = listOf("**/*.kts"),
    val excludes: List<String> = listOf("**/build/**/*.kts")
)

data class SpotlessXmlRules(
    val targets: List<String> = listOf("**/*.xml"),
    val excludes: List<String> = listOf("**/build/**/*.xml")
)

internal fun Project.applySpotless(spotlessConfig: SpotlessOptions) {
    val project = this

    if (spotlessConfig.hookOnBuild) {
        applySpotlessForBuildTask()
    }

    apply<SpotlessPlugin>()

    extensions.configure<SpotlessExtension> {
        setupKotlin(
            config = spotlessConfig.kotlinRules,
            project = project
        )

        setupKotlinDsl(spotlessConfig.kotlinFileRules)

        setupXml(spotlessConfig.xmlRules)
    }

    rootProject.apply {
        subprojects {
            apply<SpotlessPlugin>()

            extensions.configure<SpotlessExtension> {
                setupKotlin(
                    config = spotlessConfig.kotlinRules,
                    project = project
                )

                setupKotlinDsl(spotlessConfig.kotlinFileRules)

                setupXml(spotlessConfig.xmlRules)
            }
        }
    }
}

private fun Project.applySpotlessForBuildTask() {
    tasks.named("build").configure {
        dependsOn(tasks.withType<SpotlessApply>())
    }
}

private fun SpotlessExtension.setupKotlin(config: SpotlessKotlinRules, project: Project) {
    kotlin {
        target(config.targets)
        targetExclude(config.excludes)
        ktlint()
            .userData(config.userData)
            .setEditorConfigPath("${project.rootDir}/.editorconfig")
        endWithNewline()
    }
}

private fun SpotlessExtension.setupKotlinDsl(config: SpotlessKotlinFileRules) {
    format(config.fileExtension) {
        target(config.targets)
        targetExclude(config.excludes)
    }
}

private fun SpotlessExtension.setupXml(config: SpotlessXmlRules?) {
    config?.let {
        format("xml") {
            target(config.targets)
            targetExclude(config.excludes)
        }
    }
}
