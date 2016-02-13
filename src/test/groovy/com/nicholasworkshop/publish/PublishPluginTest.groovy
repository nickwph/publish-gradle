package com.nicholasworkshop.publish

import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException
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
                sonatype; bintray
            }
        }
        project.evaluate()
        // verify
        assertTrue project.tasks.getNames().contains("publishSonatype")
        assertNotNull project.tasks.getByName("publishSonatype")
        assertNotNull project.tasks.getByName("publishBintray")
    }

    @Test
    public void testApply_mavenDetails() throws Exception {
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

    @Test(expectedExceptions = TaskExecutionException)
    public void testExecute() throws Exception {
        Project project = ProjectUtils.createJavaProject()
        project.apply(plugin: 'com.nicholasworkshop.publish')
        project.publish {
            id 'id'
            group 'group'
            version 'version'
            releaseConfirm false
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
        project.tasks.getByName("publishSonatype").execute()
    }
}