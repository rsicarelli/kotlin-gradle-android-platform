package com.rsicarelli.kplatform

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
    AndroidAppOptionsBuilder().apply(appOptionsBuilder).build(),
    CompilationOptionsBuilder().apply(compilationOptionsBuilder).build()
)

fun Project.androidLibrary(
    compilationOptionsBuilder: CompilationOptionsBuilder.() -> Unit = { },
    libraryOptionsBuilder: AndroidLibraryOptionsBuilder.() -> Unit = { },
) = applyAndroidLibrary(
    AndroidLibraryOptionsBuilder().apply(libraryOptionsBuilder).build(),
    CompilationOptionsBuilder().apply(compilationOptionsBuilder).build()
)

fun Project.jvmLibrary(builderAction: CompilationOptionsBuilder.() -> Unit = { }) = applyJvmLibrary(
    CompilationOptionsBuilder().apply(builderAction).build()
)
