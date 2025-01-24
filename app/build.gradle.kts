val isLocalBuild = if(project.findProperty("local.build") == "true") true else false
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.lavinou.startpointapp"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.lavinou.startpointapp"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    if(isLocalBuild) {
        implementation(platform(project(":startpoint:bom")))
        implementation(project(":startpoint:core"))
        implementation(project(":startpoint:auth"))
        implementation(project(":startpoint:auth:password"))
        implementation(project(":startpoint:auth:biometric"))
    } else {
        implementation(platform("com.github.lavinou.startpoint-android:bom:0.4.0"))
        implementation("com.github.lavinou.startpoint-android:core")
        implementation("com.github.lavinou.startpoint-android:auth")
        implementation("com.github.lavinou.startpoint-android:auth-password")
        implementation("com.github.lavinou.startpoint-android:auth-biometric")
    }

//    implementation(project(":startpoint:auth:passkey"))

    // serialization
    implementation(libs.kotlinx.serialization)

    // api
    implementation(libs.bundles.networking)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.compose)


    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)
    implementation(libs.google.material)

    debugImplementation(libs.compose.ui.tooling)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}