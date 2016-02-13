package com.nicholasworkshop.publish

import org.gradle.api.Project
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static com.nicholasworkshop.publish.MavenTarget.*
import static java.lang.String.format
import static org.junit.Assert.assertEquals

/**
 * Created by nickwph on 2/5/16.
 */
class MavenTargetTest {

    private Project project;
    private MavenTarget target;

    @BeforeMethod
    public void setUp() throws Exception {
        project = ProjectUtils.createJavaProject()
        target = new MavenTarget("");
    }

    @Test
    public void testValidate() throws Exception {
        target.id 'id'
        target.url 'url'
        target.username 'username'
        target.password 'password'
        target.validate(project)
        assertEquals target.id, 'id'
        assertEquals target.url, 'url'
        assertEquals target.username, 'username'
        assertEquals target.password, 'password'
        assertEquals target.snapshotId, 'id'
        assertEquals target.snapshotUrl, 'url'
        assertEquals target.snapshotUsername, 'username'
        assertEquals target.snapshotPassword, 'password'
    }

    @Test
    public void testValidate_ifSnapshotInfoSet() throws Exception {
        target.id 'id'
        target.url 'url'
        target.username 'username'
        target.password 'password'
        target.snapshotId 'snapshotId'
        target.snapshotUrl 'snapshotUrl'
        target.snapshotUsername 'snapshotUsername'
        target.snapshotPassword 'snapshotPassword'
        target.validate(project)
        assertEquals target.id, 'id'
        assertEquals target.url, 'url'
        assertEquals target.username, 'username'
        assertEquals target.password, 'password'
        assertEquals target.snapshotId, 'snapshotId'
        assertEquals target.snapshotUrl, 'snapshotUrl'
        assertEquals target.snapshotUsername, 'snapshotUsername'
        assertEquals target.snapshotPassword, 'snapshotPassword'
    }

    @Test(dataProvider = 'targetNameToId')
    public void testValidate_prePopulateData(String name, String id, String url, String snapshotUrl) throws Exception {
        target = new MavenTarget(name)
        target.username 'username'
        target.packageName 'package'
        target.validate(project)
        assertEquals target.id, id
        assertEquals target.snapshotId, id
        assertEquals target.url, url;
        assertEquals target.snapshotUrl, snapshotUrl;
    }

    @DataProvider
    public Object[][] targetNameToId() {
        return [['bintray', 'bintray-username-maven', format(BINTRAY_RELEASE_URL, 'username', 'package'), BINTRAY_SNAPSHOT_URL],
                ['jcenter', 'bintray-username-maven', format(BINTRAY_RELEASE_URL, 'username', 'package'), BINTRAY_SNAPSHOT_URL],
                ['sonatype', null, SONATYPE_RELEASE_URL, SONATYPE_SNAPSHOT_URL],
                ['whatever', null, null, null]]
    }

}