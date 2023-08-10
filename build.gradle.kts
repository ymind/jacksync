import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    signing

    kotlin("jvm")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")

    // https://plugins.gradle.org/plugin/team.yi.semantic-gitlog
    id("team.yi.semantic-gitlog") version "0.6.5"

    // https://plugins.gradle.org/plugin/se.patrikerdes.use-latest-versions
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    // https://plugins.gradle.org/plugin/com.github.ben-manes.versions
    id("com.github.ben-manes.versions") version "0.47.0"

    // https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "team.yi.jacksync"
version = "0.8.0"
description = "Jacksync provides a library for synchronization by producing and applying a JSON patches to Java objects. " +
        "Inspired by RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch) written in Java, which uses Jackson at its core."

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenLocal()

    maven("https://repo.huaweicloud.com/repository/maven/")
    maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    maven("https://ymind-maven.pkg.coding.net/repository/emtboot/public/")

    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine/
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks {
    jar { enabled = true }
    test { useJUnitPlatform() }

    val kotlinSettings: KotlinCompile.() -> Unit = {
        kotlinOptions.apiVersion = "1.8"
        kotlinOptions.languageVersion = "1.8"
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xjsr305=strict"
        )
    }

    compileKotlin(kotlinSettings)
    compileTestKotlin(kotlinSettings)
    compileJava { options.encoding = "UTF-8" }
    compileTestJava { options.encoding = "UTF-8" }
    javadoc { options.encoding = "UTF-8" }

    changelog {
        group = "publishing"

        toRef = "master"

        issueUrlTemplate = "https://github.com/ymind/jacksync/issues/:issueId"
        commitUrlTemplate = "https://github.com/ymind/jacksync/commit/:commitId"
        mentionUrlTemplate = "https://github.com/:username"

        // jsonFile = file("${project.rootDir}/CHANGELOG.json")
        fileSets = setOf(
            team.yi.gradle.plugin.FileSet(
                file("${project.rootDir}/config/gitlog/CHANGELOG.md.mustache"),
                file("${project.rootDir}/CHANGELOG.md")
            ),
            team.yi.gradle.plugin.FileSet(
                file("${project.rootDir}/config/gitlog/CHANGELOG.zh-cn.md.mustache"),
                file("${project.rootDir}/CHANGELOG.zh-cn.md")
            )
        )
        commitLocales = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-locales.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md")
        )
        scopeProfiles = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-scopes.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-scopes.zh-cn.md")
        )

        outputs.upToDateWhen { false }
    }

    derive {
        group = "publishing"

        toRef = "master"
        derivedVersionMark = "NEXT_VERSION:=="

        commitLocales = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-locales.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md")
        )

        outputs.upToDateWhen { false }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/detekt/baseline.xml")
}

tasks.register("bumpVersion") {
    group = "publishing"

    dependsOn(":changelog")

    doLast {
        var newVersion = rootProject.findProperty("newVersion") as? String

        if (newVersion.isNullOrEmpty()) {
            // ^## ([\d\.]+(-SNAPSHOT)?) \(.+\)$
            val changelogContents = file("${rootProject.rootDir}/CHANGELOG.md").readText()
            val versionRegex = Regex("^## ([\\d\\.]+(-SNAPSHOT)?) \\(.+\\)\$", setOf(RegexOption.MULTILINE))
            val changelogVersion = versionRegex.find(changelogContents)?.groupValues?.get(1)

            changelogVersion?.let { newVersion = it }

            logger.warn("changelogVersion: {}", changelogVersion)
            logger.warn("newVersion: {}", newVersion)
        }

        newVersion?.let {
            logger.info("Set Project to new Version $it")

            val contents = buildFile.readText()
                .replaceFirst("version = \"$version\"", "version = \"$newVersion\"")

            buildFile.writeText(contents)
        }
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/ymind/jacksync")
                inceptionYear.set("2020")

                scm {
                    url.set("https://github.com/ymind/jacksync")
                    connection.set("scm:git:git@github.com:ymind/jacksync.git")
                    developerConnection.set("scm:git:git@github.com:ymind/jacksync.git")
                }

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                        distribution.set("repo")
                    }
                }

                organization {
                    name.set("Yi.Team")
                    url.set("https://yi.team/")
                }

                developers {
                    developer {
                        name.set("ymind")
                        email.set("ymind@yi.team")
                        url.set("https://yi.team/")
                        organization.set("Yi.Team")
                        organizationUrl.set("https://yi.team/")
                    }
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/ymind/jacksync/issues")
                }

                ciManagement {
                    system.set("GitHub")
                    url.set("https://github.com/ymind/jacksync/actions")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

            url = if (version.toString().endsWith("SNAPSHOT", true)) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = System.getenv("OSSRH_USERNAME") ?: "${properties["OSSRH_USERNAME"]}"
                password = System.getenv("OSSRH_TOKEN") ?: "${properties["OSSRH_TOKEN"]}"
            }
        }
    }
}

signing {
    extra.set("signing.keyId", System.getenv("OSSRH_GPG_SECRET_ID") ?: "${properties["OSSRH_GPG_SECRET_ID"]}")
    extra.set("signing.secretKeyRingFile", System.getenv("OSSRH_GPG_SECRET_KEY") ?: "${properties["OSSRH_GPG_SECRET_KEY"]}")
    extra.set("signing.password", System.getenv("OSSRH_GPG_SECRET_PASSWORD") ?: "${properties["OSSRH_GPG_SECRET_PASSWORD"]}")

    sign(publishing.publications.getByName("mavenJava"))
}
