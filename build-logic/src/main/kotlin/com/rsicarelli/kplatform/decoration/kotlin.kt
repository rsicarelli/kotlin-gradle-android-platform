/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.rsicarelli.kplatform.decoration

import com.rsicarelli.kplatform.options.CompilationOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.applyJvmLibrary(compilationOptions: CompilationOptions) {
    pluginManager.apply("java-library")
    applyJavaCompatibility(compilationOptions.javaVersion)
    applyKotlinOptions(compilationOptions)
}

private fun Project.applyJavaCompatibility(javaVersion: JavaVersion) {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

internal fun Project.applyKotlinOptions(compilationOptions: CompilationOptions) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = compilationOptions.allWarningsAsErrors
            jvmTarget = compilationOptions.jvmTarget
        }
    }
}
