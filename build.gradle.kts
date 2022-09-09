
repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "com.target"

dependencies {
    val kotlinCoroutinesVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")


    val junitVersion: String by project
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    val kotestVersion: String by project
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    val mockkVersion: String by project
    testImplementation("io.mockk:mockk:$mockkVersion")

}

tasks {
    compileJava { options.release.set(11) }
    compileKotlin { kotlinOptions { jvmTarget = "11" } }
    compileTestKotlin { kotlinOptions { jvmTarget = "11" } }

    withType<Test> {
        useJUnitPlatform()
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
