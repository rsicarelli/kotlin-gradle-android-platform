package com.rsicarelli.kplatform.options

data class DetektOptions(
    val parallel: Boolean,
    val configFileName: String,
    val includes: List<String>,
    val excludes: List<String>,
)

class DetektOptionsBuilder {

    var parallel: Boolean = true
    var configFileName: String = "detekt.yaml"
    var includes: List<String> = listOf("**/*.kt", "**/*.kts")
    var excludes: List<String> = listOf(".*/resources/.*", ".*/build/.*")

    internal fun build(): DetektOptions = DetektOptions(
        parallel = parallel,
        configFileName = configFileName,
        includes = includes,
        excludes = excludes
    )
}
