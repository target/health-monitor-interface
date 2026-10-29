plugins {
    id("kotlin-library-conventions")
}

group = "com.target"

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.bundles.testing)
}
