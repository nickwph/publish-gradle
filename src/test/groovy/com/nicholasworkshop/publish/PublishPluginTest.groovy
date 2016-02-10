package com.nicholasworkshop.publish

import org.gradle.api.Project
import org.gradle.api.artifacts.PublishException
import org.gradle.api.tasks.Upload
import org.testng.annotations.Test

import static org.testng.Assert.*

/**
 * Created by nickwph on 2/7/16.
 */
public class PublishPluginTest {

    @Test
    public void testApply() throws Exception {
        Project project = ProjectUtils.createJavaProject()
        project.apply(plugin: 'com.nicholasworkshop.publish')
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
            licenses 'mit', 'apache-2.0'
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
        assertTrue project.tasks.getNames().contains("publishSonatype")
        Upload upload = project.tasks.getByName("publishSonatype") as Upload
        assertNotNull upload
        assertEquals upload.repositories.mavenDeployer.repository.url, 'releaseUrl'
        assertEquals upload.repositories.mavenDeployer.repository.authentication.userName, 'username'
        assertEquals upload.repositories.mavenDeployer.repository.authentication.password, 'password'
        assertEquals upload.repositories.mavenDeployer.pom.model.licenses.size(), 2
    }

    @Test(expectedExceptions = PublishException)
    public void testExecute() throws Exception {
        Project project = ProjectUtils.createJavaProject()
        project.apply(plugin: 'com.nicholasworkshop.publish')
        project.publish {
            id 'id'
            group 'group'
            version 'version'
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
        assertTrue project.tasks.getNames().contains("publishSonatype")
        Upload upload = project.tasks.getByName("publishSonatype") as Upload
        upload.execute()
    }
}