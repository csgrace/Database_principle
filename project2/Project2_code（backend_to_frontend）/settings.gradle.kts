rootProject.name = "sustc"

include(
        "sustc-api",
        "sustc-runner",
        "sustc-spring_boot" // 添加 spring_boot 模块
)

dependencyResolutionManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        mavenCentral()
        gradlePluginPortal()
    }
}
