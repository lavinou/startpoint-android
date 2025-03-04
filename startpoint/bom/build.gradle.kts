import java.util.Date
import java.text.SimpleDateFormat

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
            api(project(":startpoint:auth:biometric"))
        } else {
            api("com.github.lavinou.startpoint-android:core:$releaseVersion")
            api("com.github.lavinou.startpoint-android:auth:$releaseVersion")
            api("com.github.lavinou.startpoint-android:auth-password:$releaseVersion")
            api("com.github.lavinou.startpoint-android:auth-biometric:$releaseVersion")
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