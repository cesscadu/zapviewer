plugins {
    id(Plugins.android_library)
    id(Plugins.kotlin_android)
    id(Plugins.kotlin_android_extensions)
    id(Plugins.kotlin_android_kapt)
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Config.version_code
        versionName = Config.version_name
        testInstrumentationRunner = Config.test_runner
    }

    buildTypes {
        getByName(BuildType.release) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

dependencies {
    implementation(project(Modules.design))
    implementation(project(Modules.common))

    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation (Deps.coil)
    implementation (Deps.kotlin_stdlib)
    implementation (Deps.androidx_appcompat)
    implementation (Deps.androidx_constraint_layout)
}