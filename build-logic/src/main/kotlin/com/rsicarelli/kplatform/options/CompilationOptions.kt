package com.rsicarelli.kplatform.options

import org.gradle.api.JavaVersion

data class CompilationOptions(
    val javaVersion: JavaVersion = JavaVersion.VERSION_17,
    val jvmTarget: String = "17",
    val allWarningsAsErrors: Boolean = false,
)

class CompilationOptionsBuilder {

    var javaVersion: JavaVersion = JavaVersion.VERSION_17
    var jvmTarget: String = "17"
    var allWarningsAsErrors: Boolean = false

    fun build(): CompilationOptions = CompilationOptions(
        javaVersion = javaVersion,
        jvmTarget = jvmTarget,
        allWarningsAsErrors = allWarningsAsErrors
    )
}
