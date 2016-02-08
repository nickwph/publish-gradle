package com.nicholasworkshop.publish

import org.apache.maven.model.License
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by nickwph on 1/28/16.
 */

class PreferencesExtension {

    private Project project
    private NamedDomainObjectContainer<MavenTarget> mavenTargets;

    String id
    String group
    String version
    String projectName
    String projectUrl
    String projectDescription
    String projectPackaging
    String scmUrl
    String scmConnection
    String scmDeveloperConnection
    String developerId
    String developerName
    License license
    boolean signing

    PreferencesExtension(Project project) {
        this.project = project
        this.mavenTargets = project.container(MavenTarget)
    }

    NamedDomainObjectContainer<MavenTarget> getMavenTargets() {
        return mavenTargets
    }

    void mavenTargets(Action<NamedDomainObjectContainer<MavenTarget>> action) {
        action.execute(mavenTargets)
    }

    void validate() {
        if (id == null && project.hasProperty('artifactId')) {
            id = project.property('artifactId')
        }
        if (group == null && project.group != null) {
            group = project.group
        }
        if (version == null && project.version != null) {
            version = project.version
        }
        if (scmUrl == null && projectUrl != null) {
            scmUrl = projectUrl
        }
        if (scmDeveloperConnection == null && scmConnection != null) {
            scmDeveloperConnection = scmConnection
        }
        mavenTargets.each { MavenTarget mavenTarget ->
            mavenTarget.validate()
        }
    }
}

