plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("org.jetbrains.kotlin.jvm") version "1.7.0-RC2"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")

    // import gurobi library:
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")

    //https://github.com/tools4j/decimal4j
    implementation("org.decimal4j:decimal4j:1.0.3")

    // https://github.com/JFormDesigner/FlatLaf
    implementation("com.formdev:flatlaf:3.0")
    implementation("com.formdev:flatlaf-intellij-themes:3.0")
}

application {
    // Define the main class for the application.
    mainClass.set("zimmerzuteilung.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}