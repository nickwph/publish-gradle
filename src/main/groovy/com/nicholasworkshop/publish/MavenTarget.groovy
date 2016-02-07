package com.nicholasworkshop.publish

import org.gradle.api.GradleException

/**
 * Created by nickwph on 1/30/16.
 */
class MavenTarget {

    private static final SONATYPE_NAME = "sonatype";
    private static final SONATYPE_RELEASE_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2";
    private static final SONATYPE_SNAPSHOT_URL = "https://oss.sonatype.org/content/repositories/snapshots";

    private static final BINTRAY_NAME = "bintray";
    private static final BINTRAY_RELEASE_URL = "https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1";
    private static final BINTRAY_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";

    String name
    String url
    String snapshotUrl
    String username
    String password
    String snapshotUsername
    String snapshotPassword

    MavenTarget(String name) {
        this.name = name

        if (name.equalsIgnoreCase(SONATYPE_NAME)) {
            url = SONATYPE_RELEASE_URL
            snapshotUrl = SONATYPE_SNAPSHOT_URL
        } else if (name.equalsIgnoreCase(BINTRAY_NAME)) {
            url = BINTRAY_RELEASE_URL
            snapshotUrl = BINTRAY_SNAPSHOT_URL
        }
    }

    void url(String url) {
        this.url = url
    }

    void snapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl
    }

    void username(String username) {
        this.username = username
    }

    void password(String password) {
        this.password = password
    }

    void snapshotUsername(String username) {
        this.snapshotUsername = username
    }

    void snapshotPassword(String password) {
        this.snapshotPassword = password
    }

    void validate() {

        if (url == null) {
            throw new GradleException("Url for target $name is not set!")
        }

        if (username == null) {
            throw new GradleException("Username for target $name is not set!")
        }

        if (password == null) {
            throw new GradleException("Password for target $name is not set!")
        }

        if (snapshotUrl == null) {
            snapshotUrl = url
        }

        if (snapshotUsername == null) {
            snapshotUsername = username
        }

        if (snapshotPassword == null) {
            snapshotPassword = password
        }
    }
}
