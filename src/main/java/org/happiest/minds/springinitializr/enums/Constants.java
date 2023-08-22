package org.happiest.minds.springinitializr.enums;

public enum Constants {

    REFERENCE_DIR_PATH("reference/"),
    DOWNLOAD_DIR_PATH("download/"),
    DOWNLOAD_DIR_MAIN_PATH("download/spring-template/src/main"),
    DOWNLOAD_DIR_TEST_PATH("download/spring-template/src/test"),
    DOWNLOAD_DIR_MAIN_JAVA_PATH("download/spring-template/src/main/java/"),
    DOWNLOAD_DIR_TEST_JAVA_PATH("download/spring-template/src/test/java/"),
    REFERENCE_DIR_MAIN_FILE_PATH("reference/spring-template/src/main/java/org/happiest/minds/springtemplate"),
    REFERENCE_DIR_TEST_FILE_PATH("reference/spring-template/src/test/java/org/happiest/minds/springtemplate"),
    MAIN_CLASS_FILENAME("/SpringTemplateApplication.java"),
    TEST_CLASS_FILENAME("/SpringTemplateApplicationTests.java"),
    APPLICATION("Application"),
    APPLICATION_TESTS("ApplicationTests"),
    SPRING_TEMPLATE_APPLICATION("SpringTemplateApplication"),
    SPRING_TEMPLATE_APPLICATION_TESTS("SpringTemplateApplicationTests"),
    JAVA(".java"),
    GROUP_ID("groupId"),
    ARTIFACT_ID("artifactId"),
    NAME("name"),
    DESCRIPTION("description"),
    PACKAGING("packaging"),
    DEPENDENCIES("dependencies"),
    DEPENDENCY("dependency"),
    EXISTING_PACKAGE_NAME("org.happiest.minds.springtemplate"),
    POM_XML_FILEPATH("download/spring-template/pom.xml"),
    PROJECT_DIR_NAME("download/spring-template"),
    DEPENDENCY_NOT_PRESENT("Any or none of the dependency present"),
    SPRING_TEMPLATE_READY_TO_DOWNLOAD("Spring template ready to download"),
    ZIP(".zip"),
    SPRING_FRAMEWORK_GROUP_ID("org.springframework.boot"),
    WEB_ARTIFACT_ID("spring-boot-starter-web"),
    GRAPHQL_ARTIFACT_ID("spring-boot-starter-graphql"),
    THYMELEAF_ARTIFACT_ID("spring-boot-starter-thymeleaf"),
    SECURITY_ARTIFACT_ID("spring-boot-starter-security"),
    JPA_ARTIFACT_ID("spring-boot-starter-data-jpa"),
    JDBC_ARTIFACT_ID("spring-boot-starter-data-jdbc"),
    MYSQL_GROUP_ID("com.mysql"),
    MYSQL_ARTIFACT_ID("mysql-connector-j"),
    H2_GROUP_ID("com.h2database"),
    H2_ARTIFACT_ID("h2"),
    VALIDATION_ARTIFACT_ID("spring-boot-starter-validation"),
    LOMBOK_GROUP_ID("org.projectlombok"),
    LOMBOK_ARTIFACT_ID("lombok"),
    SLASH("/");

    private String value;
    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
