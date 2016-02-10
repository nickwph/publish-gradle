package com.nicholasworkshop.publish

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by nickwph on 1/28/16.
 */
class PreferencesExtension {

    private final Project project
    private final List<License> licenses = new ArrayList<>()
    private final NamedDomainObjectContainer<MavenTarget> mavenTargets

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

    void mavenTargets(Closure<NamedDomainObjectContainer<MavenTarget>> closure) {
        mavenTargets.configure(closure)
    }
    
    List<License> getLicenses() {
        return licenses
    }

    void licenses(String[] names) {
        for (String name : names) {
            licenses.add(new License(name))
        }
    }

    void validate() {
        System.out.println licenses
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

