import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.hilt) // Hilt plugin
    alias(libs.plugins.ksp) // KSP plugin
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.codewithrish.pdfreader"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.codewithrish.pdfreader"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("google/protobuf/field_mask.proto")
        }
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val capName = variant.name.capitalized()
            tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                setSource(tasks.getByName("generate${capName}Proto").outputs)
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Proto Buff
    implementation(libs.protobuf.protoc) // Protobuf runtime
    implementation(libs.protobuf.kotlin.lite)  // Optional: Kotlin extensions for Protobuf

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt dependencies
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Use KSP for annotation processing
    implementation(libs.androidx.hilt.navigation.compose) // Uncomment if using Hilt Navigation Compose
    implementation(libs.androidx.compose.viewmodel) // ViewModel

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Datastore
    implementation(libs.androidx.dataStore)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Pdf Viewer
    implementation(libs.bouquet)

    // Adaptive
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.androidx.compose.material3.adaptive)

    // Material Icons
    implementation(libs.androidx.compose.material.iconsExtended)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)

    // Time Zone
    implementation(libs.kotlinx.datetime)

    // Tracing
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.tracing.ktx)

    // Permissions
    implementation(libs.accompanist.permissions)

    // Timber Logging
    implementation(libs.timber)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.remote.config)
    implementation(libs.firebase.crashlytics)

    // Joda Time
    implementation(libs.joda.time)

    // MixPanel
    implementation(libs.mixpanel.android)

    // Animated Navigation
    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")
    implementation ("androidx.compose.animation:animation:1.7.5")

}
