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
    boolean signing = false

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
        if (id == null && project.hasProperty('artifactId')) id = project.property('artifactId')
        if (group == null && project.group != null) group = project.group
        if (version == null && project.version != null) version = project.version
        if (scmUrl == null && projectUrl != null) scmUrl = projectUrl
        if (scmDeveloperConnection == null && scmConnection != null) scmDeveloperConnection = scmConnection

        // check empties
        String message = ''
        if (id == null) message += 'id '
        if (group == null) message += 'group '
        if (version == null) message += 'version '
        if (projectName == null) message += 'projectName '
        if (projectUrl == null) message += 'projectUrl '
        if (developerId == null) message += 'developerId '
        if (developerName == null) message += 'developerName '
        if (!message.isEmpty()) System.err.println "[WARNING] There are missing project info: $message"

        // validate targets
        for (MavenTarget mavenTarget in mavenTargets) {
            mavenTarget.validate(project)
        }
    }
}

