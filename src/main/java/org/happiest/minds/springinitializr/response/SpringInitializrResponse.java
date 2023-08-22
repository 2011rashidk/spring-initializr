package org.happiest.minds.springinitializr.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class SpringInitializrResponse {

    private HttpStatus httpStatus;
    private String message;
}
