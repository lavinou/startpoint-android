val isLocalBuild = if(project.findProperty("local.build") == "true") true else false
val releaseVersion: String = libs.versions.startpoint.get()

plugins {
    alias(libs.plugins.dokka)
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        if(isLocalBuild) {
            api(project(":startpoint:core"))
            api(project(":startpoint:auth"))
            api(project(":startpoint:auth:password"))
        } else {
            api("com.github.lavinou.startpoint-android:core:$releaseVersion")
            api("com.github.lavinou.startpoint-android:auth:$releaseVersion")
            api("com.github.lavinou.startpoint-android:auth-password:$releaseVersion")
            // more versions here
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            afterEvaluate {
                from(components["javaPlatform"])
            }
            groupId = "com.github.lavinou.startpoint-android"
            artifactId = "bom"
            version = releaseVersion
        }
    }
}