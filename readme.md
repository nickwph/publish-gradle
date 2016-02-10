# Publish Gradle Plugin

[![Circle CI](https://circleci.com/gh/nickwph/publish-gradle.svg?style=shield)](https://circleci.com/gh/nickwph/publish-gradle)
[![codecov.io](https://codecov.io/github/nickwph/publish-gradle/coverage.svg?branch=master)](https://codecov.io/github/nickwph/publish-gradle?branch=master)

## Recommended Usage

``` groovy
apply plugin: 'com.nicholasworkshop.artifactid'
apply plugin: 'com.nicholasworkshop.publish'

id 'artifactId'
group 'com.domain.name'
version '1.0.0'

publish {
    projectName 'projectName'
    projectUrl 'projectUrl'
    developerId 'johndoe'
    developerName 'John Doe'
    licenses 'mit', 'apache-2.0' // https://developer.github.com/v3/licenses/#list-all-licenses
    mavenTargets {
        bintray {
            username 'username'
            password 'password'
        }
    }
}
```

## Tasks Generated

| install                  |
| -------------------------|
| publish<MavenTargetName> |
| publishBintray           |
| publishSonatype          |

## Project Licenses

Please use keys defined by Github<br/>
https://developer.github.com/v3/licenses/#list-all-licenses

## Project Signing

Signing is off by default. Enable it with:
```
signing true
```

Follow these tutorials to generate PGP keys
http://central.sonatype.org/pages/working-with-pgp-signatures.html
http://blog.sonatype.com/2010/01/how-to-generate-pgp-signatures-with-maven

After that, add the following to gradle.properties (with your confidential info)
```properties
signing.keyId=<SIGNING_ID>
signing.password=<PASSWORD>
signing.secretKeyRingFile=<PATH>/.gnupg/secring.gpg
```

## Advanced Usage

``` groovy
apply plugin: 'com.nicholasworkshop.publish'

publish {
    // basicInfo
    id 'id'             
    group 'group'
    version 'version'
    // project
    projectName 'projectName'
    projectUrl 'projectUrl'
    projectDescription 'projectDescription'
    projectPackaging 'projectPackaging'
    // scm
    scmUrl 'scmUrl'
    scmConnection 'scmConnection'
    scmDeveloperConnection 'scmDeveloperConnection'
    // developer
    developerId 'developerId'
    developerName 'developerName'
    // license
    licenses 'mit', 'apache-2.0'
    // signing
    signing true
    // mavens
    mavenTargets {
        sonatype {
            url 'releaseUrl'
            id 'id'
            username 'username'
            password 'password'
            snapshotUrl 'snapshotUrl'
            snapshotId 'id'
            snapshotUsername 'username'
            snapshotPassword 'password'
        }
        bintray {
            url 'releaseUrl'
            id 'id'
            username 'username'
            password 'password'
            snapshotUrl 'snapshotUrl'
            snapshotId 'id'
            snapshotUsername 'username'
            snapshotPassword 'password'
        }
        jcenter {
            url 'releaseUrl'
            id 'id'
            username 'username'
            password 'password'
            snapshotUrl 'snapshotUrl'
            snapshotId 'id'
            snapshotUsername 'username'
            snapshotPassword 'password'
        }
        custom {
            url 'releaseUrl'
            id 'id'
            username 'username'
            password 'password'
            snapshotUrl 'snapshotUrl'
            snapshotId 'id'
            snapshotUsername 'username'
            snapshotPassword 'password'
        }
    }
}
```