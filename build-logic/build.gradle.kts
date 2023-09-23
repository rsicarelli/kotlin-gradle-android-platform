/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.detekt)
    compileOnly(libs.gradlePlugin.spotless)
}

gradlePlugin {
    plugins.register("kplatformPlugin") {
        id = "com.rsicarelli.kplatform"
        implementationClass = "com.rsicarelli.kplatform.KPlatformPlugin"
    }
}
