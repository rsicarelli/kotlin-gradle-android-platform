plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.detekt)
}

gradlePlugin {
    plugins.register("kplatformPlugin") {
        id = "com.rsicarelli.kplatform"
        implementationClass = "com.rsicarelli.kplatform.KplatformPlugin"
    }
}
