/*
 * Copyright (c) Rodrigo Sicarelli 2023.
 * SPDX-License-Identifier: Apache-2.0
 */

import org.gradle.api.internal.FeaturePreviews

enableFeaturePreview(FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS.toString())
enableFeaturePreview(FeaturePreviews.Feature.STABLE_CONFIGURATION_CACHE.toString())

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kplatform"
include(":app", ":features:details", ":features:home", ":core:designsystem", ":core:threading")
