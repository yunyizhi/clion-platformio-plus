plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.0"
}

group = "org.btik"
version = "0.0.4.0-beta"

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
            """<h3>0.0.4.0</h3>
                en:
                <p>Fixed some issues.</p>
                <p>Added some static hints about auto-completion of the value of the 'platformio.ini' property</p>
                中文:
                <p>修复了一些问题。</p>
                <p>增加了一些对 `platformio.ini` 的属性的值自动补全的相关静态提示</p>
                <h3>0.0.3.0</h3>
                <p>en:When executing commands using the task tree, multiple environment configurations existing in platformio.ini can be
                    checked. This feature is independent of the `default_envs` in platformio.ini.</p>
                <p>中文:任务树执行命令，支持勾选platformio.ini的多环境配置。该功能与platformio.ini的`default_envs`无关</p>
                <h3>0.0.2.2</h3>
                <p>en:Modify and optimize some issues.</p>
                <p>中文:修改和优化一些问题</p>
                <h3>0.0.2.1</h3>
                <p>en:Support platformio.ini prompting/autocomplete.</p>
                <p>中文:支持platformio.ini提示/自动补全</p>"""
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
