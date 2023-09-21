plugins {
    `kotlin-dsl`
}

gradlePlugin {
    val greeting by plugins.creating {
        id = "com.rsicarelli.kplatform"
        implementationClass = "com.rsicarelli.kplatform.KplatformPlugin"
    }
}
