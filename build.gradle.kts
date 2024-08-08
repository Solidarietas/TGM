plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "network.warzone"
version = "2.0-SNAPSHOT"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io")
    }
    maven("https://maven.enginehub.org/repo/")
    // Store your github username and a personal access token with permission to download packages
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/BGMP/CommandFramework")
        credentials {
            username = project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as? String ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("com.sk89q.worldedit:worldedit-core:7.2.0")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
//    compileOnly("com.github.ProtocolSupport:ProtocolSupport:master-1f834da42d-1")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("cl.bgm:command-framework-bukkit:1.0.4-SNAPSHOT")
    implementation("org.mongodb:mongo-java-driver:3.4.2")
    implementation("com.konghq:unirest-java:3.11.10") {
        exclude(group = "commons-codec")
    }
    implementation("com.google.code.gson:gson:2.8.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.10.0.202406032230-r")

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }
}



tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.shadowJar {
    // Include only specific dependencies
    dependencies {
//        include(dependency("com.google.guava:guava:30.1-jre"))
//        include(dependency("org.apache.httpcomponents:httpclient:4.5.13"))
    }
    minimize()
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}