plugins {
    `kotlin-dsl`
}

gradlePlugin {
    val greeting by plugins.creating {
        id = "build.logic.greeting"
        implementationClass = "com.rsicarelli.buildlogic.BuildLogicPlugin"
    }
}
