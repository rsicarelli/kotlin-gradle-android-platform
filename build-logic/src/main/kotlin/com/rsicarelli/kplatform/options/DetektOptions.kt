/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.rsicarelli.kplatform.options

/**
 * Alias for constructing Detekt options.
 */
typealias DetektBuilder = DetektOptionsBuilder.() -> Unit

/**
 * Represents a set of options for configuring the Detekt static code analysis tool.
 *
 * @property parallel Flag indicating if the analysis should run in parallel.
 * @property buildUponDefaultConfig Flag indicating if Detekt should build upon its default configurations.
 * @property configFileNames List of configuration file names for Detekt.
 * @property includes List of patterns for the files that should be included in the analysis.
 * @property excludes List of patterns for the files that should be excluded from the analysis.
 */
data class DetektOptions(
    val parallel: Boolean,
    val buildUponDefaultConfig: Boolean,
    val configFileNames: List<String>,
    val includes: List<String>,
    val excludes: List<String>
)

/**
 * Builder class to construct [DetektOptions].
 *
 * Offers a fluent API to configure various Detekt settings.
 */
class DetektOptionsBuilder {

    var parallel: Boolean = true
    var configFiles: List<String> = listOf(".detekt.yml, .detekt-compose.yml")
    var buildUponDefaultConfig: Boolean = true
    var includes: List<String> = listOf("**/*.kt", "**/*.kts")
    var excludes: List<String> = listOf(".*/resources/.*", ".*/build/.*")

    internal fun build(): DetektOptions = DetektOptions(
        parallel = parallel,
        configFileNames = configFiles,
        includes = includes,
        excludes = excludes,
        buildUponDefaultConfig = buildUponDefaultConfig
    )
}
