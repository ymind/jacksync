import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    `maven-publish`
    signing

    kotlin("jvm")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")

    // https://plugins.gradle.org/plugin/tech.yanand.maven-central-publish
    id("tech.yanand.maven-central-publish") version "1.3.0"

    // https://plugins.gradle.org/plugin/team.yi.semantic-gitlog
    id("team.yi.semantic-gitlog") version "0.6.5"

    // https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

group = "team.yi.jacksync"
version = "0.8.27"
description = "Jacksync provides a library for synchronization by producing and applying a JSON patches to Java objects. " +
        "Inspired by RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch) written in Java, which uses Jackson at its core."

val repoUrl = layout.buildDirectory.dir(
    if (version.toString().endsWith("SNAPSHOT", true)) {
        "repos/releases"
    } else {
        "repos/snapshots"
    },
)

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine/
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")

    // detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        jvmTarget = JvmTarget.JVM_17

        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn",
        )
    }
}

tasks {
    jar { enabled = true }
    test { useJUnitPlatform() }

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
                file("${project.rootDir}/CHANGELOG.md"),
            ),
            team.yi.gradle.plugin.FileSet(
                file("${project.rootDir}/config/gitlog/CHANGELOG.zh-cn.md.mustache"),
                file("${project.rootDir}/CHANGELOG.zh-cn.md"),
            ),
        )
        commitLocales = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-locales.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md"),
        )
        scopeProfiles = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-scopes.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-scopes.zh-cn.md"),
        )

        outputs.upToDateWhen { false }
    }

    derive {
        group = "publishing"

        toRef = "master"
        derivedVersionMark = "NEXT_VERSION:=="

        commitLocales = mapOf(
            "en" to file("${project.rootDir}/config/gitlog/commit-locales.md"),
            "zh-cn" to file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md"),
        )

        outputs.upToDateWhen { false }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    autoCorrect = true
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
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
            url = uri(repoUrl)
        }
    }
}

signing {
    extra.set("signing.keyId", System.getenv("OSSRH_GPG_SECRET_ID") ?: "${properties["OSSRH_GPG_SECRET_ID"]}")
    extra.set("signing.secretKeyRingFile", System.getenv("OSSRH_GPG_SECRET_KEY") ?: "${properties["OSSRH_GPG_SECRET_KEY"]}")
    extra.set("signing.password", System.getenv("OSSRH_GPG_SECRET_PASSWORD") ?: "${properties["OSSRH_GPG_SECRET_PASSWORD"]}")

    sign(publishing.publications.getByName("mavenJava"))
}

mavenCentral {
    authToken = System.getenv("OSSRH_AUTH_TOKEN") ?: "${properties["OSSRH_AUTH_TOKEN"]}"
    publishingType = "USER_MANAGED" // USER_MANAGED AUTOMATIC
}
