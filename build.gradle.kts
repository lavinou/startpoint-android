// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dokka) apply false
}

// Register the main publishing task
tasks.register("publishToMavenLocal") {
    dependsOn(":startpoint:auth:password:publishToMavenLocal")
    finalizedBy(":startpoint:bom:publishToMavenLocal")
}

// Enforce task order when the task graph is ready
gradle.taskGraph.whenReady {
    tasks.named("publishToMavenLocal") {
        dependsOn(
            ":startpoint:core:publishToMavenLocal",
            ":startpoint:auth:core-uieee:publishToMavenLocal"
        )
    }

    tasks.withType<PublishToMavenLocal>().configureEach {
        if (name.contains(":startpoint:auth:publishToMavenLocal")) {
            mustRunAfter(
                ":startpoint:core:publishToMavenLocal"
            )
        }
    }

    tasks.withType<PublishToMavenLocal>().configureEach {
        if (name.contains(":startpoint:auth:password:publishToMavenLocal")) {
            mustRunAfter(
                ":startpoint:auth:publishToMavenLocal"
            )
        }
    }
}

// Register the main publishing task
tasks.register("publishRemote") {
    dependsOn(":startpoint:auth:password:publish")
    finalizedBy(":startpoint:bom:publish")
}

// Enforce task order when the task graph is ready
gradle.taskGraph.whenReady {
    tasks.named("publishRemote") {
        dependsOn(
            ":startpoint:core:publish",
            ":startpoint:auth:core-uieee:publish"
        )
    }

    tasks.withType<PublishToMavenLocal>().configureEach {
        if (name.contains(":startpoint:auth:publish")) {
            mustRunAfter(
                ":startpoint:core:publish"
            )
        }
    }

    tasks.withType<PublishToMavenLocal>().configureEach {
        if (name.contains(":startpoint:auth:password:publish")) {
            mustRunAfter(
                ":startpoint:auth:publish"
            )
        }
    }
}