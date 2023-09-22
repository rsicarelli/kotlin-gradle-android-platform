package com.rsicarelli.kplatform.options

data class DetektOptions(
    val parallel: Boolean,
    val buildUponDefaultConfig: Boolean,
    val configFileNames: List<String>,
    val includes: List<String>,
    val excludes: List<String>
)

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
