import com.rsicarelli.kplatform.androidApp

plugins {
    id(libs.plugins.android.application.get().pluginId)
    kotlin("android")
}

androidApp {
    versionCode = 1
    versionName = "1.0.0"
    generateBuildConfig = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(projects.core.designsystem)
    implementation(projects.features.home)
}
