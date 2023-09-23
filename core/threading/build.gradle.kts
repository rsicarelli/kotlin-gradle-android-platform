/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.rsicarelli.kplatform.jvmLibrary

plugins {
    kotlin("jvm")
}

jvmLibrary()

dependencies {
    api(libs.kotlinx.coroutines.core)
    testApi(libs.kotlinx.coroutines.test)
}
