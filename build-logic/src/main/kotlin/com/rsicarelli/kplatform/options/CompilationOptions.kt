/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.rsicarelli.kplatform.options

import org.gradle.api.JavaVersion

internal data class CompilationOptions(
    val javaVersion: JavaVersion,
    val jvmTarget: String,
    val allWarningsAsErrors: Boolean,
)

class CompilationOptionsBuilder {

    var javaVersion: JavaVersion = JavaVersion.VERSION_17
    var jvmTarget: String = "17"
    var allWarningsAsErrors: Boolean = false

    internal fun build(): CompilationOptions = CompilationOptions(
        javaVersion = javaVersion,
        jvmTarget = jvmTarget,
        allWarningsAsErrors = allWarningsAsErrors
    )
}
