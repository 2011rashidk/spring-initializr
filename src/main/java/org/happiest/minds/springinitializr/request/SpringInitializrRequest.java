package org.happiest.minds.springinitializr.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class SpringInitializrRequest {

    @NotBlank(message = "group should not be blank")
    @Pattern(regexp = "^[a-zA-Z.]+", message = "group should contain only alphabets and dot")
    private String group;

    @NotBlank(message = "artifact should not be blank")
    @Pattern(regexp = "^[a-zA-Z.-]+", message = "artifact should contain only alphabets, dot and -")
    private String artifact;

    @NotBlank(message = "name should not be blank")
    private String name;

    @NotBlank(message = "description should not be blank")
    private String description;

    @NotBlank(message = "packagingName should not be blank")
    @Pattern(regexp = "^[a-zA-Z.]+", message = "packaging name should contain only alphabets and dot")
    private String packagingName;

    @NotBlank(message = "packagingType should not be blank")
    @Pattern(regexp = "^(jar|war)$", message = "packaging type should be jar or war")
    private String packagingType;

    @NotEmpty(message = "dependencies should not be empty")
    private List<String> dependencies;
}
