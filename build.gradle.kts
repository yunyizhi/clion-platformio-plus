plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.12.0"
}

group = "org.btik"
version = "0.0.2.2-beta"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation("junit:junit:4.13.2")
}


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.3")
    type.set("CL") // Target IDE Platform

    plugins.set(listOf("com.jetbrains.plugins.ini4idea:223.7571.233"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("222.*")
        changeNotes.set(
            "<h3>0.0.2.2</h3>\n" +
                    "<p>en:Modify and optimize some issues.</p>\n" +
                    "<p>中文:修改和优化一些问题</p>\n" +
                    "<h3>0.0.2.1</h3>\n" +
                    "<p>en:Support platformio.ini prompting/autocomplete.</p>\n" +
                    "<p>中文:支持platformio.ini提示/自动补全</p>"
        )
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
