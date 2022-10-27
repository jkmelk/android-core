import Dependencies.load

plugins {
    id("kotlin")
}

kotlin {
    dependencies {
        load(Dependencies.dataLibraries())
    }
}