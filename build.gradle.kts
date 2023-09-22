import com.rsicarelli.kplatform.detekt

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.arturbosch.detekt) apply false
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

detekt()
