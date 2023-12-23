import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    //id("org.jetbrains.compose")
    id("org.jetbrains.compose") version "1.6.0-dev1296"
}

group = "com.litecodez.archweaver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven (
        "https://packages.jetbrains.team/maven/p/skija/maven"
    )
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("media.kamel:kamel-image:0.9.0")
    api ("org.jetbrains.skija:skija-linux:0.93.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
    //implementation ("org.jetbrains.skia:skiko-jvm:0.1.0")

//    implementation("io.ktor:ktor-client-apache5:1.7.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ArchWeaver"
            packageVersion = "1.0.0"
        }
    }
}
