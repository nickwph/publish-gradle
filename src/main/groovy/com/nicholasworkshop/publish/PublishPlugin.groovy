package com.nicholasworkshop.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar

/**
 * Created by nickwph on 2/7/16.
 */
public class PublishPlugin implements Plugin<Project> {

    private static final String TASK_GROUP = 'publish'
    private static final String EXTENSION_NAME = 'publish'

    private Project project;
    private PreferencesExtension preferences;

    @Override
    public void apply(Project project) {
        this.project = project
        this.preferences = project.extensions.create(EXTENSION_NAME, PreferencesExtension, project)

        PluginContainer container = project.getPlugins()
        if (!container.hasPlugin('maven')) container.apply('maven')
        if (!container.hasPlugin('signing')) container.apply('signing')

        project.afterEvaluate {
            configureCommonTasks()
            configureInstallTask()
            preferences.validate()
            preferences.mavenTargets.each { MavenTarget target ->
                configureMavenTargetTask(target)
            }
        }
    }

    private void configureCommonTasks() {
        Jar javadoc = project.tasks.create("jarJavadoc", Jar)
        javadoc.group = 'build'
        javadoc.classifier = 'javadoc'
        javadoc.from project.javadoc
        Jar sources = project.tasks.create("jarSources", Jar)
        sources.group = 'build'
        sources.classifier = 'sources'
        sources.from project.sourceSets.main.allSource
        project.artifacts {
            archives sources, javadoc
        }
        project.signing {
            required = preferences.signing
            sign project.configurations.archives
        }

    }

    private Upload configureInstallTask() {
        Upload upload = project.tasks.getByName("install") as Upload
        upload.group = TASK_GROUP
        upload.repositories.mavenInstaller {
            pom.artifactId = preferences.id
        }
        return upload
    }

    private Upload configureMavenTargetTask(MavenTarget target) {
        String targetName = target.name.capitalize()
        String taskName = "publish${targetName}"
        Upload upload = project.tasks.create(taskName, Upload)
        upload.group = TASK_GROUP
        upload.uploadDescriptor = true
        upload.configuration = project.configurations.archives
        upload.repositories.mavenDeployer {
            pom.groupId = preferences.group
            pom.artifactId = preferences.id
            pom.version = preferences.version
            pom.project {
                url = preferences.projectUrl
                name = preferences.projectName
                packaging = preferences.projectPackaging
                description = preferences.projectDescription
                scm {
                    url preferences.scmUrl
                    connection preferences.scmConnection
                    developerConnection preferences.scmDeveloperConnection
                }
                licenses {
                    for (License lic in preferences.licenses) {
                        license {
                            name lic.name
                            url lic.url
                            distribution '?'
                        }
                    }
                }
                developers {
                    developer {
                        id preferences.developerId
                        name preferences.developerName
                    }
                }
            }
            beforeDeployment { MavenDeployment deployment ->
                project.signing.signPom(deployment)

            }
        }
        String url = !preferences.version.endsWith('SNAPSHOT') ? target.url : target.snapshotUrl
        if (url != null) {
            upload.repositories.mavenDeployer.repository(url: url) {
                authentication(userName: target.username, password: target.password)
            }
        }
        return upload
    }
}
