plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}