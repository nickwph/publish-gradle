package com.nicholasworkshop.publish

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.Upload
import org.junit.Assert
import org.junit.Test

/**
 * Created by nickwph on 2/7/16.
 */
public class PublishPluginTest {

    @Test(expected = GradleException.class)
    public void testApply() throws Exception {
        Project project = ProjectUtils.createJavaProject()
        project.apply(plugin: 'com.nicholasworkshop.publish')
        project.ext.artifactId = 'id'
        project.publish {
            id 'id'
            group 'group'
            version 'version'
            projectName 'projectName'
            projectUrl 'projectUrl'
            projectDescription 'projectDescription'
            projectPackaging 'projectPackaging'
            scmUrl 'scmUrl'
            scmConnection 'scmConnection'
            scmDeveloperConnection 'scmDeveloperConnection'
            developerId 'developerId'
            developerName 'developerName'
            signing false
            mavenTargets {
                sonatype {
                    url 'releaseUrl'
                    snapshotUrl 'snapshotUrl'
                    username 'username'
                    password 'password'
                }
            }
        }
        project.evaluate()

        // verify
        Assert.assertTrue(project.tasks.getNames().contains("publishSonatype"))
        Upload upload = project.tasks.getByName("publishSonatype") as Upload
        Assert.assertNotNull(upload)
        Assert.assertEquals('releaseUrl', upload.repositories.mavenDeployer.repository.url)
        Assert.assertEquals('username', upload.repositories.mavenDeployer.repository.authentication.userName)
        Assert.assertEquals('password', upload.repositories.mavenDeployer.repository.authentication.password)
        upload.execute()
    }
}