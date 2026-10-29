import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.diffplug.spotless")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        allWarningsAsErrors.set(true)
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_4)
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

spotless {
    kotlin {
        ktlint().setEditorConfigPath("$rootDir/.editorconfig")
    }
    kotlinGradle {
        ktlint().setEditorConfigPath("$rootDir/.editorconfig")
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
        }
        jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
    }
}
