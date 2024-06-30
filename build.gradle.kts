import org.jetbrains.changelog.Changelog

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

// Gradle IntelliJ Plugin
// https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("PC-2022.2.5")
    plugins.set(listOf(/* Plugin Dependencies */))
}

// Gradle Changelog Plugin
// https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    path.set(file("CHANGELOG.md").canonicalPath)
    header = provider { "v${version.get()}" }
    headerParserRegex.set("""v(\d+.\d+.\d+)""".toRegex())
}

// https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#tasks
tasks {
    // JVM 兼容版本
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    initializeIntelliJPlugin {
        // selfUpdateCheck = false
    }
    runIde {
        jvmArgs = listOf(
            // "-Duser.language=en_US",
            // "-XX:+UnlockDiagnosticVMOptions",
        )
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

dependencies{
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation("org.xerial:sqlite-jdbc:3.21.0.1")

    // https://mvnrepository.com/artifact/org.mybatis/mybatis
    implementation("org.mybatis:mybatis:3.5.7"){
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    implementation("org.slf4j:slf4j-nop:1.7.30")

}