plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
}
gradlePlugin {
    val greeting by plugins.creating {
        id = "com.rsicarelli.kplatform"
        implementationClass = "com.rsicarelli.kplatform.KplatformPlugin"
    }
}
