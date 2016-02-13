package com.nicholasworkshop.publish

import groovy.swing.SwingBuilder
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

import static java.io.File.pathSeparator

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
            preferences.validate()
            configureSigningTask()
            if (container.hasPlugin('com.android.application') || container.hasPlugin('com.android.library')) {
                configureAndroidCommonTasks()
            } else {
                configureCommonTasks()
            }
            configureInstallTask()
            preferences.mavenTargets.each { MavenTarget target ->
                configureMavenTargetTask(target, true)
                if (!preferences.version.endsWith('-SNAPSHOT')) {
                    // generate release task only if it is not snapshot already
                    configureMavenTargetTask(target, false)
                }
            }
        }
    }

    private void configureSigningTask() {
        project.signing {
            required = preferences.signing
            sign project.configurations.archives
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
            archives sources
            archives javadoc
        }
    }

    private void configureAndroidCommonTasks() {
        Javadoc javadocAndroid = project.tasks.create('javadocAndroid', Javadoc)
        javadocAndroid.group = 'build'
        javadocAndroid.source = project.android.sourceSets.main.java.srcDirs
        javadocAndroid.classpath += project.files(project.android.bootClasspath.join(pathSeparator))

        Jar javadoc = project.tasks.create("jarJavadoc", Jar)
        javadoc.dependsOn javadocAndroid
        javadoc.group = 'build'
        javadoc.classifier = 'javadoc'
        javadoc.from javadocAndroid.destinationDir

        Jar sources = project.tasks.create("jarSources", Jar)
        sources.group = 'build'
        sources.classifier = 'sources'
        sources.from project.android.sourceSets.main.java.sourceFiles

        project.artifacts {
            archives sources
            archives javadoc
        }
    }

    private Upload configureInstallTask() {
        Upload upload = project.tasks.findByName("install") as Upload
        if (upload == null) {
            upload = project.tasks.create('install', Upload)
            upload.uploadDescriptor = true
            upload.configuration = project.configurations.archives
        }
        upload.group = TASK_GROUP
        upload.repositories.mavenInstaller {
            pom.artifactId = preferences.id
        }
        return upload
    }

    private Upload configureMavenTargetTask(MavenTarget target, boolean isSnapshot) {
        String targetName = target.name.capitalize()
        String taskName = "publish${targetName}${isSnapshot ? 'Snapshot' : ''}"

        Upload upload = project.tasks.create(taskName, Upload)
        upload.group = TASK_GROUP
        upload.uploadDescriptor = true
        upload.configuration = project.configurations.archives
        upload.repositories.mavenDeployer {
            pom.groupId = preferences.group
            pom.artifactId = preferences.id
            pom.version = "${preferences.version}${isSnapshot && !preferences.version.endsWith('-SNAPSHOT') ? '-SNAPSHOT' : ''}"
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
                if (preferences.signing) {
                    project.signing.signPom(deployment)
                }
                if (!isSnapshot && preferences.releaseConfirm) {
                    String answer = prompt '> Sure to deploy as release? (yes/no)'
                    if (!'yes'.equalsIgnoreCase(answer)) throw new GradleException('Publish canceled')
                }
            }
        }

        String id = !isSnapshot ? target.id : target.snapshotId
        String url = !isSnapshot ? target.url : target.snapshotUrl
        String username = !isSnapshot ? target.username : target.snapshotUsername
        String password = !isSnapshot ? target.password : target.snapshotPassword
        if (url != null) {
            upload.repositories.mavenDeployer.repository(id: id, url: url) {
                authentication(userName: username, password: password)
            }
        }
        return upload
    }

    private static String prompt(String question) {
        def console = System.console()
        if (console == null) {
            String answer = ''
            new SwingBuilder().edt {
                dialog(modal: true, title: 'Confirmation', alwaysOnTop: true, resizable: false, locationRelativeTo: null, pack: true, show: true) {
                    vbox {
                        label(text: question)
                        button(text: 'OK', actionPerformed: {
                            dispose();
                            answer = 'yes'
                        })
                        button(defaultButton: true, text: 'Cancel', actionPerformed: {
                            dispose();
                            answer = 'no'
                        })
                    }
                }
            }
            return answer
        } else {
            return console.readLine('> Sure to deploy as release? (yes/no)')
        }
    }
}
