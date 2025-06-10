plugins {
    id("java")
}

group = "miroshka"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.lanink.cn/repository/maven-public/")
}

dependencies {
    compileOnly("cn.nukkit:Nukkit:MOT-SNAPSHOT")
}

tasks.jar {
    archiveFileName.set("AntiAFK-${project.version}.jar")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    doLast {
        copy {
            from(archiveFile)
            into(rootDir)
        }
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            "version" to project.version
        )
    }
}

tasks.test {
    useJUnitPlatform()
}