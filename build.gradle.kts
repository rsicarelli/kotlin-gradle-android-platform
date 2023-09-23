plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}
