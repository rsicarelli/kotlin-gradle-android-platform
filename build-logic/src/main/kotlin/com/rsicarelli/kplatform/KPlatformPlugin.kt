package com.rsicarelli.kplatform

import com.rsicarelli.kplatform.decoration.applyAndroidApp
import com.rsicarelli.kplatform.decoration.applyAndroidLibrary
import com.rsicarelli.kplatform.decoration.applyDetekt
import com.rsicarelli.kplatform.decoration.applyJvmLibrary
import com.rsicarelli.kplatform.decoration.applySpotless
import com.rsicarelli.kplatform.options.AndroidAppBuilder
import com.rsicarelli.kplatform.options.AndroidAppOptionsBuilder
import com.rsicarelli.kplatform.options.AndroidLibraryBuilder
import com.rsicarelli.kplatform.options.AndroidLibraryOptionsBuilder
import com.rsicarelli.kplatform.options.CompilationBuilder
import com.rsicarelli.kplatform.options.CompilationOptionsBuilder
import com.rsicarelli.kplatform.options.DetektBuilder
import com.rsicarelli.kplatform.options.DetektOptionsBuilder
import com.rsicarelli.kplatform.options.SpotlessBuilder
import com.rsicarelli.kplatform.options.SpotlessOptionsBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project

class KPlatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = Unit
}

fun Project.androidApp(
    compilationBuilder: CompilationBuilder = { },
    appBuilder: AndroidAppBuilder = { }
) = applyAndroidApp(
    androidAppOptions = AndroidAppOptionsBuilder().apply(appBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationBuilder).build()
)

fun Project.androidLibrary(
    compilationBuilder: CompilationBuilder = { },
    libraryBuilder: AndroidLibraryBuilder = { }
) = applyAndroidLibrary(
    androidLibraryOptions = AndroidLibraryOptionsBuilder().apply(libraryBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationBuilder).build()
)

fun Project.jvmLibrary(compilationBuilder: CompilationBuilder = { }) =
    applyJvmLibrary(CompilationOptionsBuilder().apply(compilationBuilder).build())

fun Project.detekt(builderAction: DetektBuilder = {}) =
    applyDetekt(DetektOptionsBuilder().apply(builderAction).build())

fun Project.spotless(builderAction: SpotlessBuilder = { }) =
    applySpotless(SpotlessOptionsBuilder().apply(builderAction).build())
