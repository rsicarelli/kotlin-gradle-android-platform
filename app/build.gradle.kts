/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.rsicarelli.kplatform.androidApp

plugins {
    id(libs.plugins.android.application.get().pluginId)
    kotlin("android")
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

androidApp()

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(projects.core.designsystem)
    implementation(projects.features.home)
}
