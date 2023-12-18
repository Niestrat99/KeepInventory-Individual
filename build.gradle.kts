plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.2.0"
}

group = "io.github.niestrat99"
version = project.property("plugin_version")!!

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation("com.mysql:mysql-connector-j:8.2.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
    }

    processResources {
        inputs.property("plugin_version", project.version)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(Pair("plugin_version", project.version))
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}