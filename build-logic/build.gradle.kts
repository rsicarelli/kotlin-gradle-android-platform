plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
}
gradlePlugin {
    plugins.register("kplatformPlugin") {
        id = "com.rsicarelli.kplatform"
        implementationClass = "com.rsicarelli.kplatform.KplatformPlugin"
    }
}
