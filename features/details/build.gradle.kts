/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.rsicarelli.kplatform.androidLibrary
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalCoroutinesApi

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
}

androidLibrary(
    compilationBuilder = {
        optIn(ExperimentalCoroutinesApi)
    }
)

dependencies {
    implementation(projects.core.designsystem)
}
