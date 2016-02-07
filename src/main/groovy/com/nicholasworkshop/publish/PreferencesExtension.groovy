package com.nicholasworkshop.publish

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
/**
 * Created by nickwph on 1/28/16.
 */

class PreferencesExtension {

    private Project project
    private NamedDomainObjectContainer<MavenTarget> mavenTargets;

    // artifact info
    String id
    String group
    String version

    // project info
    String projectName
    String projectUrl
    String projectDescription
    String projectPackaging

    String scmUrl
    String scmConnection
    String scmDeveloperConnection

    String developerId
    String developerName

    String license

    boolean signing

    NamedDomainObjectContainer<MavenTarget> getMavenTargets() {
        return mavenTargets
    }

    public PreferencesExtension(Project project) {
        this.project = project
        this.mavenTargets = project.container(MavenTarget)
    }

    public void mavenTargets(Action<NamedDomainObjectContainer<MavenTarget>> action) {
        action.execute(mavenTargets)
    }

    void validate() {

        if (id == null) {
            if (project.plugins.hasPlugin('com.nicholasworkshop.artifactid') && project.hasId()) {
                id = project.getId()
            } else if (project.hasProperty('artifactId')) {
                id = project.ext.artifactId
            } else {
                throw new GradleException("Artifact id not set!")
            }
        }

        if (group == null) {
            if (project.group != null) {
                group = project.group
            } else {
                throw new GradleException("Group id not set!")
            }
        }

        if (version == null) {
            if (project.version != null) {
                version = project.version
            } else {
                throw new GradleException("Version not set!")
            }
        }

        if (projectName == null) {
            throw new GradleException("Project name not set!")
        }

        mavenTargets.each { MavenTarget mavenTarget ->
            mavenTarget.validate()
        }
    }

}

