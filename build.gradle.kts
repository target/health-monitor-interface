plugins {
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    // The root project has no group, so the plugin can't look up a staging profile without this.
    packageGroup.set("com.target")
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}