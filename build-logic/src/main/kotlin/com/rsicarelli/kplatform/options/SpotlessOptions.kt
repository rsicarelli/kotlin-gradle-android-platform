/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.rsicarelli.kplatform.options

/**
 * Alias for constructing Spotless options.
 */
typealias SpotlessBuilder = SpotlessOptionsBuilder.() -> Unit

/**
 * Represents a set of options for configuring the Spotless code formatter.
 *
 * @property fileRules A list of rules for how files should be formatted by Spotless.
 */
data class SpotlessOptions(
    val fileRules: List<SpotlessFileRule> = listOf(SpotlessKtsRule, SpotlessXmlRule),
)

/**
 * Interface defining a rule for file formatting using Spotless.
 *
 * @property fileExtension The file extension that the rule applies to.
 * @property targets Patterns for the files that should be formatted.
 * @property excludes Patterns for the files that should be excluded from formatting.
 */
interface SpotlessFileRule {

    val fileExtension: String
    val targets: List<String>
    val excludes: List<String>
}

/**
 * A [SpotlessFileRule] for formatting Kotlin Script files.
 */
object SpotlessKtsRule : SpotlessFileRule {

    override val fileExtension: String = "kts"
    override val targets: List<String> = listOf("**/*.kts")
    override val excludes: List<String> = listOf("**/build/**/*.kts")
}

/**
 * A [SpotlessFileRule] for formatting XML files.
 */
object SpotlessXmlRule : SpotlessFileRule {

    override val fileExtension: String = "xml"
    override val targets: List<String> = listOf("**/*.xml")
    override val excludes: List<String> = listOf("**/build/**/*.xml")
}

/**
 * Builder class to construct [SpotlessOptions].
 *
 * Offers a fluent API to configure various Spotless settings.
 */
class SpotlessOptionsBuilder {

    var fileRules: List<SpotlessFileRule> = listOf(SpotlessKtsRule, SpotlessXmlRule)

    internal fun build(): SpotlessOptions = SpotlessOptions(
        fileRules = fileRules
    )
}
