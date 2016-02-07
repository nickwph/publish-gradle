package com.nicholasworkshop.publish

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
/**
 * Created by nickwph on 2/5/16.
 */
class MavenTargetTest {

    private MavenTarget target;

    @Before
    public void setUp() throws Exception {
        target = new MavenTarget("");
    }

    @Test
    public void test_ifSonatypeIsUsedAsName_thenPrepopulateUrls() throws Exception {
        target = new MavenTarget("sonatype")
        assertEquals(MavenTarget.SONATYPE_RELEASE_URL, target.url);
        assertEquals(MavenTarget.SONATYPE_SNAPSHOT_URL, target.snapshotUrl);
    }
}