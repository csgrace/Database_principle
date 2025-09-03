import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.lombok)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.53")
    implementation("org.postgresql:postgresql:42.7.4")

    // You may add any utility library you want to use, such as guava.
    // ORM libraries are prohibited in this project.
}

tasks.withType<BootRun> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.register("submitJar") {
    group = "application"
    description = "Prepare an uber-JAR for submission"

    tasks.getByName<ShadowJar>("shadowJar") {
        archiveFileName = "sustc-api.jar"
        destinationDirectory = File("$rootDir/submit")
        dependencies {
            exclude(dependency("ch.qos.logback:logback-.*"))
        }
    }.let { dependsOn(it) }
}

tasks.clean {
    delete(fileTree("$rootDir/submit").matching { include("*.jar") })
}

tasks.register<JavaExec>("runGoodLoader") {
    group = "application"
    description = "Run the GoodLoader main class"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.pubmed.service.impl.GoodLoader")
    //./gradlew :sustc-api:runGoodLoader --stacktraceargs = listOf("arg1", "arg2") // Add any arguments if needed
}

// dependencies {
//     // ...existing dependencies...
//     compileOnly 'org.projectlombok:lombok:1.18.24'
//     annotationProcessor 'org.projectlombok:lombok:1.18.24'
//     testCompileOnly 'org.projectlombok:lombok:1.18.24'
//     testAnnotationProcessor 'org.projectlombok:lombok:1.18.24'
// }

// plugins {
//     id 'java'
// }

// repositories {
//     mavenCentral()
// }

// dependencies {
//     testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
//     testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
// }

// test {
//     useJUnitPlatform()
// }
//tasks.named("runGoodLoader", JavaExec) {
//    jvmArgs = ["-Xms512m", "-Xmx2g"] // 最小512MB，最大2GB
//}
