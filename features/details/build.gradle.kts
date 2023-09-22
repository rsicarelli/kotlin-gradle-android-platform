import com.rsicarelli.kplatform.androidLibrary
import com.rsicarelli.kplatform.options.CompilationOptions.FeatureOptIn.ExperimentalCoroutinesApi

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
}

androidLibrary(
    compilationOptionsBuilder = {
        optIn(ExperimentalCoroutinesApi)
    }
)

dependencies {
    implementation(projects.core.designsystem)
}
