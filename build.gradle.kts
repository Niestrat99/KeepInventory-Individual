plugins {
    id("java")
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

    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    implementation("com.mysql:mysql-connector-j:8.2.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {

    processResources {
        inputs.property("plugin_version", project.version)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(Pair("plugin_version", project.version))
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}