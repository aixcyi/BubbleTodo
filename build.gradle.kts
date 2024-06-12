import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.changelog") version "2.2.0"
}

group = "cn.aixcyi.plugin"
version = "0.0.1"  // https://semver.org/lang/zh-CN/

repositories {
    mavenLocal()
    mavenCentral()
}

// 配置 Gradle IntelliJ Plugin
// https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("PC-2022.2.5")
    plugins.set(listOf(/* Plugin Dependencies */))
}

// 配置 Gradle Changelog Plugin
// https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    path.set(file("CHANGELOG.md").canonicalPath)
    header = provider { "v${version.get()}，${date("yyyy-mm-dd")}" }
    headerParserRegex.set("""v(\d+.\d+.\d+)""".toRegex())
}

tasks {
    // JVM 兼容版本
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("242.*")
        pluginDescription.set(file("DESCRIPTION.html").readText())
        changeNotes.set(provider {
            changelog.renderItem(
                changelog.getLatest().withHeader(false).withEmptySections(false),
                Changelog.OutputType.HTML
            )
        })
    }

    signPlugin {
        certificateChainFile.set(file("./.secret/chain.crt"))
        privateKeyFile.set(file("./.secret/private.pem"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}