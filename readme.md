# Publish Gradle Plugin
[![Circle CI](https://img.shields.io/circleci/project/nickwph/publish-gradle.svg)]
(https://circleci.com/gh/nickwph/publish-gradle)
[![codecov.io](https://img.shields.io/codecov/c/github/nickwph/publish-gradle.svg)]
(https://codecov.io/github/nickwph/publish-gradle)
[ ![Download](https://img.shields.io/bintray/v/nickwph/maven/publish-gradle.svg)]
(https://bintray.com/nickwph/maven/publish-gradle/_latestVersion)

## Recommended Usage

``` groovy
buildscript {
    dependencies {
        classpath 'com.nicholasworkshop:gradle-artifactid:1.0.0'
        classpath 'com.nicholasworkshop:gradle-publish:1.0.0'
    }
}

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
    licenses 'mit', 'apache-2.0' // https://developer.github.com/v3/licenses
    mavenTargets {
        bintray; sonatype
    }
}
```

Put your username & password in <code>gradle.properties</code> and keep them ignored.

```properties
publish.maven.bintray.username=<USERNAME>
publish.maven.bintray.password=<API_KEY> // little reminder: bintray use apiKey as password
publish.maven.sonatype.username=<USERNAME>
publish.maven.sonatype.password=<PASSWORD>
```


## Pre-set Repository Info

There are some pre-defined maven targets with following info. They will be automatically applied if the specific target names are used.

| Target   | Id                       | Repository URL                                                           | Snapshot Repository URL                                   |
| -------  | ------------------------ | ------------------------------------------------------------------------ | --------------------------------------------------------- |
| bintray  | bintray-<code>\<USERNAME\></code>-maven | https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1 | https://oss.jfrog.org/artifactory/list/oss-snapshot-local |
| jcenter  | bintray-<code>\<USERNAME\></code>-maven | https://api.bintray.com/maven/nickwph/maven/artifactid-gradle/;publish=1 | https://oss.jfrog.org/artifactory/list/oss-snapshot-local |
| sonatype |                          | https://oss.sonatype.org/service/local/staging/deploy/maven2             | https://oss.sonatype.org/content/repositories/snapshots   |

## Repository Username & Password

You can either put your maven information in your gradle.properties or <code>build.gradle</code> (not recommended).

### Create gradle.properties file

```properties
publish.maven.<TARGET_NAME>.id=<ID>
publish.maven.<TARGET_NAME>.url=<URL>
publish.maven.<TARGET_NAME>.username=<USERNAME>
publish.maven.<TARGET_NAME>.password=<PASSWORD>
publish.maven.<TARGET_NAME>.snapshotId=<SNAPSHOT_ID>
publish.maven.<TARGET_NAME>.snapshotUrl=<SNAPSHOT_URL>
publish.maven.<TARGET_NAME>.snapshotUsername=<SNAPSHOT_USERNAME>
publish.maven.<TARGET_NAME>.snapshotPassword=<SNAPSHOT_PASSWORD>

```

### In gradle.build

```
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
```

## Tasks Generated

| Task                                  | Description                                       |
| ------------------------------------- | ------------------------------------------------- |
| install                               | Install the project archive into local maven      |
| publish<code>\<MavenTargetName\></code><br/>  publishBintray<br/> publishSonatype                       | Publish to the maven target defined, and will not  generate if <code>-SNAPSHOT</code> is already in  <code>version</code>                              |
| publish<code>\<MavenTargetName\></code>Snapshot<br/> publishBintraySnapshot<br/> publishSonatypeSnapshot               | Publish to the maven snapshot target defined      |

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
buildscript {
    dependencies {
        classpath 'com.nicholasworkshop:gradle-publish:1.0.0'
    }
}

apply plugin: 'com.nicholasworkshop.publish'

publish {
    // basicInfo
    id 'id'           // project.ext.artifactId will be used if null
    group 'group'     // project.group will be used if null
    version 'version' // project.version will be used if null
    // project
    projectName 'projectName'
    projectUrl 'projectUrl'
    projectDescription 'projectDescription'
    projectPackaging 'projectPackaging'
    // scm
    scmUrl 'scmUrl' // projectUrl will be used if null
    scmConnection 'scmConnection'
    scmDeveloperConnection 'scmDeveloperConnection'
    // developer
    developerId 'developerId'
    developerName 'developerName'
    // license
    licenses 'mit', 'apache-2.0' // https://developer.github.com/v3/licenses
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
