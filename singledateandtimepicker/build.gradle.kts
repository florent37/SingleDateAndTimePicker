plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.5.31"
}


val PUBLISH_GROUP_ID: String by extra("se.warting.datepicker")
val PUBLISH_VERSION: String by extra(rootProject.version as String)
val PUBLISH_ARTIFACT_ID by extra("singledateandtimepicker")

apply(from = "${rootProject.projectDir}/gradle/publish-module.gradle")

android {
    compileSdk = 31

    defaultConfig {
        minSdk=  14
        targetSdk=  28
    }
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

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
}
