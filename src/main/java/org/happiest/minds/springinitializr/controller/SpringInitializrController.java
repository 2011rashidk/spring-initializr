package org.happiest.minds.springinitializr.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.ErrorResponse;
import org.happiest.minds.springinitializr.service.SpringInitializrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/spring/initializr")
@Slf4j
public class SpringInitializrController {

    @Autowired
    SpringInitializrService springInitializrService;

    @GetMapping("zip")
    public ResponseEntity<?> downloadTemplate(@Valid @RequestBody SpringInitializrRequest springInitializrRequest,
                                              BindingResult bindingResult) {
        log.info("springInitializrRequest: {}", springInitializrRequest);
        if (bindingResult.hasErrors()) {
            log.error("Binding result errors: {}", bindingResult.getAllErrors());
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        springInitializrService.downloadTemplate(springInitializrRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
