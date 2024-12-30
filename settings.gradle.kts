pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "StartPointApp"
include(":app")
include(":startpoint")
include(":startpoint:auth")
include(":startpoint:auth:password")
include(":startpoint:auth:passkey")
include(":startpoint:auth:core-ui")
include(":startpoint:core")
include(":startpoint:bom")
