package com.rsicarelli.kplatform

import com.rsicarelli.kplatform.decoration.applyAndroidApp
import com.rsicarelli.kplatform.decoration.applyAndroidLibrary
import com.rsicarelli.kplatform.decoration.applyJvmLibrary
import com.rsicarelli.kplatform.options.AndroidAppOptionsBuilder
import com.rsicarelli.kplatform.options.AndroidLibraryOptionsBuilder
import com.rsicarelli.kplatform.options.CompilationOptionsBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project

class KplatformPlugin : Plugin<Project> {

    override fun apply(project: Project) = Unit
}

fun Project.androidApp(
    compilationOptionsBuilder: CompilationOptionsBuilder.() -> Unit = { },
    appOptionsBuilder: AndroidAppOptionsBuilder.() -> Unit = { },
) = applyAndroidApp(
    androidAppOptions = AndroidAppOptionsBuilder().apply(appOptionsBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationOptionsBuilder).build()
)

fun Project.androidLibrary(
    compilationOptionsBuilder: CompilationOptionsBuilder.() -> Unit = { },
    libraryOptionsBuilder: AndroidLibraryOptionsBuilder.() -> Unit = { },
) = applyAndroidLibrary(
    androidLibraryOptions = AndroidLibraryOptionsBuilder().apply(libraryOptionsBuilder).build(),
    compilationOptions = CompilationOptionsBuilder().apply(compilationOptionsBuilder).build()
)

fun Project.jvmLibrary(builderAction: CompilationOptionsBuilder.() -> Unit = { }) =
    applyJvmLibrary(
        compilationOptions = CompilationOptionsBuilder().apply(builderAction).build()
    )
