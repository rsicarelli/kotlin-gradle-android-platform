import com.rsicarelli.kplatform.androidLibrary
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalCoroutinesApi

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

androidLibrary(
    compilationOptionsBuilder = {
        optIn(ExperimentalCoroutinesApi)
    }
)

dependencies {
    implementation(projects.core.designsystem)
}
