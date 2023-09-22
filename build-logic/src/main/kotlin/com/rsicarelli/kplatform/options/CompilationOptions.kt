package com.rsicarelli.kplatform.options

import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn
import org.gradle.api.JavaVersion

data class CompilationOptions(
    val javaVersion: JavaVersion,
    val jvmTarget: String,
    val allWarningsAsErrors: Boolean,
    val featureOptIns: List<FeatureOptIn>
) {

    val extraFreeCompilerArgs: List<String>
        get() = featureOptIns.map { "-opt-in=${it.flag}" }

    enum class FeatureOptIn(val flag: String) {
        ExperimentalMaterial3("androidx.compose.material3.ExperimentalMaterial3Api"),
        ExperimentalCoroutinesApi(flag = "kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

class CompilationOptionsBuilder {

    var javaVersion: JavaVersion = JavaVersion.VERSION_17
    var jvmTarget: String = "17"
    var allWarningsAsErrors: Boolean = false
    private val featureOptInsBuilder = FeatureOptInBuilder()

    fun optIn(vararg optIn: FeatureOptIn) {
        featureOptInsBuilder.apply {
            featureOptIns = optIn.toList()
        }
    }

    internal fun build(): CompilationOptions = CompilationOptions(
        javaVersion = javaVersion,
        jvmTarget = jvmTarget,
        allWarningsAsErrors = allWarningsAsErrors,
        featureOptIns = featureOptInsBuilder.build()
    )
}

class FeatureOptInBuilder {

    var featureOptIns: List<FeatureOptIn> = mutableListOf()

    internal fun build(): List<FeatureOptIn> = featureOptIns.toList()
}
