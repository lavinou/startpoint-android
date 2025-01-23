val isLocalBuild = if(project.findProperty("local.build") == "true") true else false

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.lavinou.startpoint.auth.biometric"
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

    if(isLocalBuild) {
        implementation(project(":startpoint:core"))
        implementation(project(":startpoint:auth"))
        implementation(project(":startpoint:auth:core-ui"))
    } else {
        implementation(libs.startpoint.core)
        implementation(libs.startpoint.auth)
        implementation(libs.startpoint.auth.core.ui)
    }

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.google.material)
    
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    //Credentials
    api(libs.bundles.credentials)

    // serialization
    implementation(libs.kotlinx.serialization)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.test.ext.junit)
    androidTestImplementation(libs.android.test.espresso)
}