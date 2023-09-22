import com.rsicarelli.kplatform.androidLibrary
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalMaterial3

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

androidLibrary(
    compilationOptionsBuilder = {
        optIn(ExperimentalMaterial3)
    }
)

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.features.details)
}
