import com.vanniktech.maven.publish.MavenPublishBaseExtension
import java.io.File
import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.plugins.signing.SigningExtension

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    // 0.36+ requires Gradle 9 + AGP 8.13; this repo uses Gradle 8.14 / AGP 8.11
    id("com.vanniktech.maven.publish") version "0.35.0" apply false
}

val publishSecretsFile = rootProject.file("publishToMaven/secrets.properties")
if (publishSecretsFile.exists()) {
    val props = Properties()
    publishSecretsFile.inputStream().use { props.load(it) }
    allprojects {
        props.forEach { name, value ->
            val key = name.toString()
            if (findProperty(key) == null) {
                extensions.extraProperties.set(key, value.toString())
            }
        }
    }
}

// --- Maven publish (from publishToMaven/maven-publish-convention.gradle.kts) ---
// Inlined here: `apply(from = that file)` loads a second plugin classpath and breaks Vanniktech.

private fun resolveGpgExecutable(project: Project): String {
    val fromEnv = System.getenv("GPG_PATH")?.trim().orEmpty()
    if (fromEnv.isNotEmpty()) {
        val f = File(fromEnv)
        if (f.canExecute()) return f.absolutePath
        if (fromEnv == "gpg" || fromEnv == "gpg2") return fromEnv
    }
    val fromProp = project.findProperty("signing.gnupg.executable")?.toString()?.trim().orEmpty()
    if (fromProp.isNotEmpty()) return fromProp
    for (c in listOf("/opt/homebrew/bin/gpg", "/opt/homebrew/bin/gpg2", "/usr/local/bin/gpg", "/usr/local/bin/gpg2")) {
        if (File(c).canExecute()) return c
    }
    return "gpg"
}

val publishGroup = providers.gradleProperty("MAVEN_PUBLISH_GROUP").orElse("com.example.library").get()
val publishVersion = providers.gradleProperty("MAVEN_PUBLISH_VERSION").orElse("0.1.0").get()
val moduleNames =
    providers.gradleProperty("MAVEN_PUBLISH_MODULES").orElse("").get()
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()

val pomName = providers.gradleProperty("MAVEN_PUBLISH_POM_NAME").orElse("Library").get()
val pomDescription = providers.gradleProperty("MAVEN_PUBLISH_POM_DESCRIPTION").orElse("Android library").get()
val pomUrl = providers.gradleProperty("MAVEN_PUBLISH_POM_URL").orElse("https://github.com/example/example").get()
val licenseName = providers.gradleProperty("MAVEN_PUBLISH_LICENSE_NAME").orElse("The Apache License, Version 2.0").get()
val licenseUrl = providers.gradleProperty("MAVEN_PUBLISH_LICENSE_URL")
    .orElse("https://www.apache.org/licenses/LICENSE-2.0.txt").get()
val developerId = providers.gradleProperty("MAVEN_PUBLISH_DEVELOPER_ID").orElse("developer").get()
val developerName = providers.gradleProperty("MAVEN_PUBLISH_DEVELOPER_NAME").orElse("Developer").get()
val developerUrl = providers.gradleProperty("MAVEN_PUBLISH_DEVELOPER_URL").orElse(pomUrl).get()
val scmUrl = providers.gradleProperty("MAVEN_PUBLISH_SCM_URL").orElse(pomUrl).get()
val scmConnection = providers.gradleProperty("MAVEN_PUBLISH_SCM_CONNECTION")
    .orElse("scm:git:git://github.com/example/example.git").get()
val scmDevConnection = providers.gradleProperty("MAVEN_PUBLISH_SCM_DEV_CONNECTION")
    .orElse("scm:git:ssh://git@github.com/example/example.git").get()
val inceptionYearProp = providers.gradleProperty("MAVEN_PUBLISH_INCEPTION_YEAR").orElse("2025").get()

val folderRepoRelative = providers.gradleProperty("MAVEN_PUBLISH_FOLDER_REPO").orElse("build/maven-repo").get()
val useSigning =
    providers.gradleProperty("MAVEN_PUBLISH_SIGNING").orElse("true").get().toBoolean()
/** If set, used as Maven artifactId instead of the Gradle project name (e.g. `toastx` vs `:toastxLib`). */
val mavenArtifactIdOverride =
    providers.gradleProperty("MAVEN_PUBLISH_ARTIFACT_ID").orElse("").get()

subprojects {
    if (moduleNames.isEmpty() || name !in moduleNames) {
        return@subprojects
    }

    group = publishGroup
    version = publishVersion

    plugins.apply("com.vanniktech.maven.publish")

    afterEvaluate {
        val mavenArtifactId = mavenArtifactIdOverride.ifBlank { project.name }
        extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
            coordinates(publishGroup, mavenArtifactId, publishVersion)
            // Do not call publishToMavenCentral() here: when SONATYPE_HOST=CENTRAL_PORTAL is set
            // (e.g. from publish-central.sh), vanniktech already configures Central; calling again
            // fails with "The value for this property is final". Use SONATYPE_AUTOMATIC_RELEASE via -P.
            if (useSigning) {
                signAllPublications()
            }
            pom {
                name.set("$pomName: $mavenArtifactId")
                description.set("$pomDescription — $mavenArtifactId")
                inceptionYear.set(inceptionYearProp)
                url.set(pomUrl)
                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                        distribution.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                        url.set(developerUrl)
                    }
                }
                scm {
                    url.set(scmUrl)
                    connection.set(scmConnection)
                    developerConnection.set(scmDevConnection)
                }
            }
        }

        if (useSigning) {
            val gpgExecutable = resolveGpgExecutable(project)
            extensions.extraProperties.set("signing.gnupg.executable", gpgExecutable)
            extensions.configure<SigningExtension>("signing") {
                useGpgCmd()
            }
        }

        extensions.configure<PublishingExtension>("publishing") {
            repositories {
                maven {
                    name = "MavenFolderRepo"
                    url = uri(rootProject.layout.projectDirectory.dir(folderRepoRelative))
                }
            }
        }
    }
}
