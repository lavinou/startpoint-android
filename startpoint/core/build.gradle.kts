plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    `maven-publish`
}

android {
    namespace = "com.lavinou.startpoint"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.extension.get()
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    api(libs.bundles.navigation)
    implementation(libs.google.material)

    implementation(libs.kotlinx.serialization)

    debugImplementation(libs.compose.ui.tooling)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)
}


//Include jar with the lib's KDoc HTML.
val kdocJar by tasks.registering(Jar::class) {
    val htmlTask = tasks["dokkaHtml"]
    dependsOn(htmlTask)

    // Create the Jar from the generated HTML files.
    from(htmlTask)
    archiveClassifier.set("javadoc")
}

val releaseAar by tasks.registering(Jar::class) {
    val build = tasks["build"]
    dependsOn(build)
    from(build)
    archiveClassifier.set("releaseaar")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            artifact(kdocJar)
            artifact(releaseAar)
            artifact("${layout.buildDirectory.get().asFile.path}/outputs/aar/core-release.aar")
            groupId = "com.github.lavinou.startpoint-android"
            artifactId = "core"
            version = libs.versions.startpoint.get()

            pom {
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.api.get().allDependencies.forEach {
                        if (it.group != null && it.version != null) {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                            dependencyNode.appendNode("scope", "compile")
                        }
                    }
                }
            }
        }
    }
}