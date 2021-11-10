plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 14
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }

    // The demo-app uses butterknife which require java8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    android {
        lint {
            baseline(file("lint-baseline.xml"))
            isCheckReleaseBuilds = true
            isCheckAllWarnings = true
            isWarningsAsErrors = true
            isAbortOnError = true
            disable.add("LintBaseline")
            disable.add("GradleDependency")
            isCheckDependencies = true
            isCheckGeneratedSources = false
            sarifOutput = file("../lint-results-app.sarif")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("com.jakewharton:butterknife:10.2.1")
    annotationProcessor("com.jakewharton:butterknife-compiler:10.2.1")
    implementation(project(":singledateandtimepicker"))
}