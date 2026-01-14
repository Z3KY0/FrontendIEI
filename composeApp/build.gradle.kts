import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("plugin.serialization") version "1.9.22"
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("io.ktor:ktor-client-logging:2.3.7")
            implementation("ch.qos.logback:logback-classic:1.4.14")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("io.ktor:ktor-client-cio:2.3.7")
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("org.jxmapviewer:jxmapviewer2:2.8") // Stable and available
            implementation(compose.components.resources)
            implementation("org.jetbrains.compose.components:components-resources:1.5.11") // O la versión que uses

            // ESTA ES LA IMPORTANTE PARA LOS ICONOS:
            implementation("org.jetbrains.compose.material:material-icons-extended:1.5.11")
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.frontend_iei.MainKt"
        buildTypes.release.proguard {
            isEnabled = false
        }
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.frontend_iei"
            packageVersion = "1.0.0"
        }
    }
}