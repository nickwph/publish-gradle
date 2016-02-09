package com.nicholasworkshop.publish

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.atLeastOnce
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify
import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull

/**
 * Created by nickwph on 2/8/16.
 */
class PreferencesExtensionTest {

    private Project project;
    private PreferencesExtension extension;

    @BeforeMethod
    public void setUp() throws Exception {
        project = ProjectUtils.createJavaProject()
        extension = new PreferencesExtension(project)
    }

    @Test
    public void testMavenTargets() throws Exception {
        extension.mavenTargets {
            sonatype
            bintray
            jcenter
        }
        assertEquals extension.mavenTargets.size(), 3
        assertNotNull extension.mavenTargets.getByName('sonatype')
        assertNotNull extension.mavenTargets.getByName('bintray')
        assertNotNull extension.mavenTargets.getByName('jcenter')
    }

    @Test
    public void testMavenTargets_whenActionUsed() throws Exception {
        extension.mavenTargets(new Action<NamedDomainObjectContainer<MavenTarget>>() {
            @Override
            void execute(NamedDomainObjectContainer<MavenTarget> mavenTargets) {
                mavenTargets.add(new MavenTarget('sonatype'))
                mavenTargets.add(new MavenTarget('bintray'))
            }
        })
        assertEquals extension.mavenTargets.size(), 2
        assertNotNull extension.mavenTargets.getByName('sonatype')
        assertNotNull extension.mavenTargets.getByName('bintray')
    }

    @Test
    public void testMavenTargets_zeroTargetsByDefault() throws Exception {
        assertEquals extension.mavenTargets.size(), 0
    }

    @Test
    public void testValidate() throws Exception {
        project.ext.artifactId = 'artifactId'
        project.group 'group'
        project.version 'version'
        extension.projectUrl = 'projectUrl'
        extension.scmConnection = 'scmConnection'
        extension.validate()
        assertEquals extension.id, 'artifactId'
        assertEquals extension.group, 'group'
        assertEquals extension.version, 'version'
        assertEquals extension.scmUrl, 'projectUrl'
        assertEquals extension.scmDeveloperConnection, 'scmConnection'
    }

    @Test
    public void testValidate_hasTargets() throws Exception {
        MavenTarget target = spy(new MavenTarget('sonatype'))
        extension.mavenTargets(new Action<NamedDomainObjectContainer<MavenTarget>>() {
            @Override
            void execute(NamedDomainObjectContainer<MavenTarget> mavenTargets) {
                mavenTargets.add target
            }
        })
        extension.validate()
        verify(target, atLeastOnce()).validate()
    }
}