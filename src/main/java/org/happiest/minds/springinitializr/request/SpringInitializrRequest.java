package org.happiest.minds.springinitializr.request;

import lombok.Data;

@Data
public class SpringInitializrRequest {

    private String group;
    private String artifact;
    private String name;
    private String description;
    private String packagingName;
    private String packagingType;
    private String[] dependencies;
}
