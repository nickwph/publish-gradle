package com.nicholasworkshop.publish

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
    static final BINTRAY_RELEASE_URL = "https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1";
    static final BINTRAY_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";
    static final BINTRAY_ID = "bintray-%s-maven";

    String id
    String name
    String url
    String username
    String password
    String snapshotId
    String snapshotUrl
    String snapshotUsername
    String snapshotPassword

    MavenTarget(String name) {
        this.name = name
        if (name.equalsIgnoreCase(SONATYPE_NAME)) {
            url = SONATYPE_RELEASE_URL
            snapshotUrl = SONATYPE_SNAPSHOT_URL
        } else if (name.equalsIgnoreCase(BINTRAY_NAME) || name.equalsIgnoreCase(BINTRAY_NAME_2)) {
            url = BINTRAY_RELEASE_URL
            snapshotUrl = BINTRAY_SNAPSHOT_URL
        }
    }

    void id(String id) {
        this.id = id
    }

    void url(String url) {
        this.url = url
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

    void validate() {
        if (snapshotId == null && id != null) {
            snapshotId = id
        }
        if (snapshotUrl == null && url != null) {
            snapshotUrl = url
        }
        if (snapshotUsername == null && username != null) {
            snapshotUsername = username
        }
        if (snapshotPassword == null && password != null) {
            snapshotPassword = password
        }
        if (name.equalsIgnoreCase(BINTRAY_NAME) || name.equalsIgnoreCase(BINTRAY_NAME_2)) {
            if (id == null && username != null) {
                id = format BINTRAY_ID, username
            }
            if (snapshotId == null && snapshotUsername != null) {
                snapshotId = format BINTRAY_ID, snapshotUsername
            }
        }
    }
}
