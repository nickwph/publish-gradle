package com.nicholasworkshop.publish

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.junit.Assert.assertEquals

/**
 * Created by nickwph on 2/5/16.
 */
class MavenTargetTest {

    private MavenTarget target;

    @BeforeMethod
    public void setUp() throws Exception {
        target = new MavenTarget("");
    }

    @Test
    public void test_ifSonatypeIsUsedAsName_thenPrepopulateUrls() throws Exception {
        target = new MavenTarget("sonatype")
        assertEquals(MavenTarget.SONATYPE_RELEASE_URL, target.url);
        assertEquals(MavenTarget.SONATYPE_SNAPSHOT_URL, target.snapshotUrl);
    }

    @Test
    public void testValidate() throws Exception {
        target.url 'url'
        target.username 'username'
        target.password 'password'
        target.validate()
        assertEquals target.url, 'url'
        assertEquals target.username, 'username'
        assertEquals target.password, 'password'
        assertEquals target.snapshotUrl, 'url'
        assertEquals target.snapshotUsername, 'username'
        assertEquals target.snapshotPassword, 'password'
    }

    @Test
    public void testValidate_ifSnapshotInfoSet() throws Exception {
        target.url 'url'
        target.username 'username'
        target.password 'password'
        target.snapshotUrl 'snapshotUrl'
        target.snapshotUsername 'snapshotUsername'
        target.snapshotPassword 'snapshotPassword'
        target.validate()
        assertEquals target.url, 'url'
        assertEquals target.username, 'username'
        assertEquals target.password, 'password'
        assertEquals target.snapshotUrl, 'snapshotUrl'
        assertEquals target.snapshotUsername, 'snapshotUsername'
        assertEquals target.snapshotPassword, 'snapshotPassword'
    }
}