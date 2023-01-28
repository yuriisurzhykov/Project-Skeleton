import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinAndroid)
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = ProjectConfigs.applicationId
    compileSdk = ProjectConfigs.compileVersion

    defaultConfig {
        applicationId = ProjectConfigs.applicationId
        minSdk = ProjectConfigs.minSdkVersion
        targetSdk = ProjectConfigs.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val properties = Properties()
            FileInputStream(file("signing.properties")).use { stream ->
                properties.load(stream)
            }
            storeFile = file(properties.getProperty("keystoreFile"))
            storePassword = properties.getProperty("keystorePassword").toString()
            keyAlias = properties.getProperty("keyAlias").toString()
            keyPassword = properties.getProperty("keyPassword").toString()
        }
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Dependencies.Android.androidCoreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.materialComponents)
    implementation(Dependencies.Android.constraintLayout)
    testImplementation(Dependencies.Testing.JUnit4)
    androidTestImplementation(Dependencies.Testing.androidJUnit4)
    androidTestImplementation(Dependencies.Testing.espressoCore)
}