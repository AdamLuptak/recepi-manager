plugins {
    idea
    id("java")
    id("org.springframework.boot") version "2.7.3"
    id("org.liquibase.gradle") version "2.1.1"
    id("com.google.cloud.tools.jib") version "3.2.1"
}

group = "eu.rebase"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val querydslVersion = "5.0.0"

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")

    implementation("org.projectlombok:lombok:1.18.24")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.liquibase:liquibase-core")

    annotationProcessor("org.projectlombok:lombok:1.18.24")

    annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion:jpa")
    annotationProcessor("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    annotationProcessor("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.mapstruct:mapstruct:1.5.2.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.2.Final")

    implementation("com.querydsl:querydsl-core:$querydslVersion")
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")

    liquibaseRuntime("org.liquibase:liquibase-core:4.2.2")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:2.1.1")
    liquibaseRuntime("org.postgresql:postgresql:42.4.0")
}



testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation("org.testcontainers:postgresql:1.17.3")
                implementation("io.rest-assured:rest-assured")
                implementation("org.springframework.boot:spring-boot-starter-jdbc")

            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/changelog/db.changelog-master.xml",
            "url" to "jdbc:postgresql://localhost:5432/",
            "username" to "postgres",
            "password" to "postgres"
        )
    }
}

jib {
    from {
        image = "registry.access.redhat.com/ubi8/openjdk-11-runtime@sha256:593e4d54645662e98db5dbcc9a31a363f294132c44aa220c4ebd2863882cce32"
    }

    to {
        image = "eu.rebase/recipe-be"
        tags = setOf("latest", version.toString())
    }

    container {
        ports = listOf("8080")
        mainClass = "eu.rebase.recipe.Application"
    }
}