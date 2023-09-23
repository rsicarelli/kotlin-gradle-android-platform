/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.rsicarelli.kplatform.androidLibrary

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

androidLibrary()

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.features.details)
}
