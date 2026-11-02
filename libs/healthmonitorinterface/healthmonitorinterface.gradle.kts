
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    signing
}

group = "com.target"

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockk)

}

val jvmTargetVersion = project.property("jvmTargetVersion").toString()
tasks {
    withType<JavaCompile> { options.release.set(jvmTargetVersion.toInt()) }
    compileKotlin { compilerOptions { jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion)) } }
    compileTestKotlin { compilerOptions { jvmTarget.set(JvmTarget.fromTarget(jvmTargetVersion)) } }

    withType<Test> {
        useJUnitPlatform()
    }
}


java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            credentials(PasswordCredentials::class)
            name = "sonatype" // correlates with the environment variable set in the github action release.yml publish job
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }

    publications {
        val projectTitle = project.property("projectTitle").toString()
        val projectDescription = project.property("projectDescription").toString()
        val projectUrl = project.property("projectUrl").toString()
        val projectScm = project.property("projectScm").toString()

        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(projectTitle)
                description.set(projectDescription)
                url.set(projectUrl)
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("ossteam")
                        name.set("OSS Office")
                        email.set("ossteam@target.com")
                    }
                }
                scm {
                    url.set(projectScm)
                }
            }
        }
    }

}

signing {
    val signingKey = project.findProperty("signingKey")?.toString()
    val signingPassword = project.findProperty("signingPassword")?.toString()
    if (signingKey.isNullOrBlank() || signingPassword.isNullOrBlank()) {
        isRequired = false
    } else {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}
