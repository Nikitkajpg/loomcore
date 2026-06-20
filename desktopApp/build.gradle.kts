import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "com.njpg.loomcore.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "LoomCore"
            packageVersion = "1.2.1"
            description = "Sewing production management software"
            vendor = "NJPG"

            windows {
                shortcut = true
                dirChooser = true
                menuGroup = "LoomCore"

                iconFile.set(project.file("src/main/resources/logo.ico"))
            }
        }
    }
}