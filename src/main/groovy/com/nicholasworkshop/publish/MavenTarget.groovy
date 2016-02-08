package com.nicholasworkshop.publish
/**
 * Created by nickwph on 1/30/16.
 */
class MavenTarget {

    static final SONATYPE_NAME = "sonatype";
    static final SONATYPE_RELEASE_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2";
    static final SONATYPE_SNAPSHOT_URL = "https://oss.sonatype.org/content/repositories/snapshots";

    static final BINTRAY_NAME = "bintray";
    static final BINTRAY_RELEASE_URL = "https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1";
    static final BINTRAY_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";

    static final JCENTER_NAME = "jcenter";
    static final JCENTER_RELEASE_URL = "https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1";
    static final JCENTER_SNAPSHOT_URL = "https://oss.jfrog.org/artifactory/list/oss-snapshot-local";

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
        } else if (name.equalsIgnoreCase(JCENTER_NAME)) {
            url = JCENTER_RELEASE_URL
            snapshotUrl = JCENTER_SNAPSHOT_URL
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
        if (snapshotUrl == null && url != null) {
            snapshotUrl = url
        }
        if (snapshotUsername == null && username != null) {
            snapshotUsername = username
        }
        if (snapshotPassword == null && password != null) {
            snapshotPassword = password
        }
    }
}
