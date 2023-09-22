import com.rsicarelli.kplatform.jvmLibrary

plugins {
    kotlin("jvm")
}

jvmLibrary()

dependencies {
    api(libs.kotlinx.coroutines.core)
    testApi(libs.kotlinx.coroutines.test)
}
