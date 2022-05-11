import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask
plugins {
    kotlin("jvm") version "1.5.10"
    application
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "me.trdin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("mysql:mysql-connector-java:8.0.28")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(buildDir.resolve("../documentation"))
    dokkaSourceSets {
        named("main") {
            moduleName.set("moduleName")//setmodulenamedisplayedinthefinaloutput
           //includes.from("Module.md")//listoffileswithmoduleandpackagedocumentation
        }
    }
}
