plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "org.btik"
version = "0.0.6.4-beta"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation("junit:junit:4.13.2")
}


// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3.4")
    type.set("CL") // Target IDE Platform

    plugins.set(
        listOf(
            "com.jetbrains.plugins.ini4idea:233.11799.244",
            "com.intellij.clion",
            "com.intellij.cidr.base",
            "intellij.clion.embedded.platformio:233.11799.171"
        )
    )
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("241.*")
        changeNotes.set(
            """<h3>0.0.6.4</h3>
                en:
                <p>Fixed the issue introduced in version 0.0.6.3.</p>
                中文:
                <p>修复了0.0.6.3引入的问题</p>
                <h3>0.0.6.3</h3>
                en:
                <ul>
                    <li>Fixed some issues.</li>
                    <li>Introduced the issue where the Pio Plus tool window is unavailable after a project is created and opened.</li>
                </ul>
                <br>
                中文:
                <ul>
                    <li>修复了一些问题.</li>
                    <li>引入了创建项目打开没有Pio Plus 的工具窗口的问题.</li>
                </ul>
                <h3>0.0.6.0</h3>
                Compatible with PlatformIO for CLion 232.8660.142.<br>
                兼容官方PlatformIO插件232.8660.142
                <h3>0.0.5.0</h3>
                en:
                <p>Support Run Configuration.</p>
                <p>Added a menu to switch the current `CMAKE_BUILD_TYPE` in the status bar.</p>
                <p>Fixed the issue where Floating Toolbar could not be automatically displayed after version 2023.</p>
                中文:
                <p>支持运行配置。</p>
                <p>在状态栏增加了切换当前`CMAKE_BUILD_TYPE`的菜单。</p>
                <p>修复了悬浮工具栏在2023版后的不能自动显示的问题。</p>
                <h3>0.0.4.0</h3>
                en:
                <p>Fixed some issues.</p>
                <p>Added some static hints about auto-completion of the value of the 'platformio.ini' property.</p>
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
