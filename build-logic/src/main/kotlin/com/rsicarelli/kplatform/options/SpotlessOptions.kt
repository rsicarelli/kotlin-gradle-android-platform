@file:Suppress("MemberVisibilityCanBePrivate")

package com.rsicarelli.kplatform.options

typealias SpotlessBuilder = SpotlessOptionsBuilder.() -> Unit

data class SpotlessOptions(
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

class SpotlessOptionsBuilder {

    var fileRules: List<SpotlessFileRule> = listOf(SpotlessKtRule, SpotlessXmlRule)

    internal fun build(): SpotlessOptions = SpotlessOptions(
        fileRules = fileRules
    )
}
