plugins {
    kotlin("jvm")
}

group = "com.xaverianteamrobotics"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}