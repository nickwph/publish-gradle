package com.nicholasworkshop.publish

import org.gradle.api.Project

import static java.lang.String.format

/**
 * Created by nickwph on 1/30/16.
 */
class MavenTarget {

    static final SONATYPE_NAME = "sonatype";
    static final SONATYPE_RELEASE_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2";
    static final SONATYPE_SNAPSHOT_URL = "https://oss.sonatype.org/content/repositories/snapshots";

    static final BINTRAY_NAME = "bintray";
    static final BINTRAY_NAME_2 = "jcenter";
    static final BINTRAY_RELEASE_URL = "https://api.bintray.com/maven/%s/maven/%s/;publish=1";
    static final BINTRAY_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";
    static final BINTRAY_ID = "bintray-%s-maven";

    static final JFROG_NAME = "jfrog";
    static final JFROG_RELEASE_URL = "https://oss.jfrog.org/artifactory/oss-release-local";
    static final JFROG_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";

    String id
    String name
    String url
    String packageName // for bintray
    String username
    String password
    String snapshotId
    String snapshotUrl
    String snapshotUsername
    String snapshotPassword

    MavenTarget(String name) {
        this.name = name
        if (SONATYPE_NAME.equalsIgnoreCase(name)) {
            url = SONATYPE_RELEASE_URL
            snapshotUrl = SONATYPE_SNAPSHOT_URL
        } else if (BINTRAY_NAME.equalsIgnoreCase(name) || BINTRAY_NAME_2.equalsIgnoreCase(name)) {
            url = BINTRAY_RELEASE_URL
            snapshotUrl = BINTRAY_SNAPSHOT_URL
        } else if (JFROG_NAME.equalsIgnoreCase(name)) {
            url = JFROG_RELEASE_URL
            snapshotUrl = JFROG_SNAPSHOT_URL
        }
    }

    void id(String id) {
        this.id = id
    }

    void url(String url) {
        this.url = url
    }

    void packageName(String packageName) {
        this.packageName = packageName
    }

    void username(String username) {
        this.username = username
    }

    void password(String password) {
        this.password = password
    }

    void snapshotId(String snapshotId) {
        this.snapshotId = snapshotId
    }

    void snapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl
    }

    void snapshotUsername(String username) {
        this.snapshotUsername = username
    }

    void snapshotPassword(String password) {
        this.snapshotPassword = password
    }

    void validate(Project project) {
        String prefix = "publish.maven.$name"

        // release
        if (id == null && project.hasProperty("${prefix}.id")) id = project.property("${prefix}.id")
        if (url == null && project.hasProperty("${prefix}.url")) url = project.property("${prefix}.url")
        if (packageName == null && project.hasProperty("${prefix}.packageName")) packageName = project.property("${prefix}.packageName")
        if (username == null && project.hasProperty("${prefix}.username")) username = project.property("${prefix}.username")
        if (password == null && project.hasProperty("${prefix}.password")) password = project.property("${prefix}.password")
        if (BINTRAY_NAME.equalsIgnoreCase(name) || BINTRAY_NAME_2.equalsIgnoreCase(name)) {
            if (id == null && username != null) id = format BINTRAY_ID, username
            if (BINTRAY_RELEASE_URL.equalsIgnoreCase(url)) url = format BINTRAY_RELEASE_URL, username, packageName
        }

        // snapshot
        if (snapshotId == null && project.hasProperty("${prefix}.snapshotId")) snapshotId = project.property("${prefix}.snapshotId")
        if (snapshotUrl == null && project.hasProperty("${prefix}.snapshotUrl")) snapshotUrl = project.property("${prefix}.snapshotUrl")
        if (snapshotUsername == null && project.hasProperty("${prefix}.snapshotUsername")) snapshotUsername = project.property("${prefix}.snapshotUsername")
        if (snapshotPassword == null && project.hasProperty("${prefix}.snapshotPassword")) snapshotPassword = project.property("${prefix}.snapshotPassword")
        if (snapshotId == null && id != null) snapshotId = id
        if (snapshotUrl == null && url != null) snapshotUrl = url
        if (snapshotUsername == null && username != null) snapshotUsername = username
        if (snapshotPassword == null && password != null) snapshotPassword = password

        // check empties
        String message = ''
        if (url == null) message += 'url '
        if (username == null) message += 'username '
        if (password == null) message += 'password '
        if (snapshotUrl == null) message += 'snapshotUrl '
        if (snapshotUsername == null) message += 'snapshotUsername '
        if (snapshotPassword == null) message += 'snapshotPassword '
        if (BINTRAY_NAME.equalsIgnoreCase(name) || BINTRAY_NAME_2.equalsIgnoreCase(name)) {
            if (id == null) message += 'id '
            if (packageName == null) message += 'packageName '
        }
        if (!message.isEmpty()) System.err.println "[WARNING] There are missing info for target $name: $message"
    }
}