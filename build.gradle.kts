import com.rsicarelli.kplatform.detekt
import com.rsicarelli.kplatform.spotless

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.diffplug.spotless) apply false
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

detekt()
spotless()
