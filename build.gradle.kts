plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("com.h2database:h2:1.4.200")
    runtimeOnly("com.h2database:h2:2.2.220")
}

tasks.test {
    useJUnitPlatform()
}