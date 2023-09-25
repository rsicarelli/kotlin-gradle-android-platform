/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.rsicarelli.kplatform.androidLibrary
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalCoroutinesApi
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalMaterial3

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
}

androidLibrary(
    compilationOptionsBuilder = {
        optIn(ExperimentalCoroutinesApi, ExperimentalMaterial3)
    }
)

dependencies {
    implementation(projects.core.designsystem)
}
