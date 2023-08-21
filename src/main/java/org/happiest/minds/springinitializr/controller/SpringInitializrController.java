package org.happiest.minds.springinitializr.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.happiest.minds.springinitializr.constant.Constants;
import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.response.SpringInitializrResponse;
import org.happiest.minds.springinitializr.service.SpringInitializrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/spring/initializr")
@Slf4j
public class SpringInitializrController {

    @Autowired
    SpringInitializrService springInitializrService;

    @GetMapping("zip")
    public ResponseEntity<?> downloadTemplate(HttpServletResponse httpServletResponse,
                                              @Valid @RequestBody SpringInitializrRequest springInitializrRequest) {
        log.info("springInitializrRequest: {}", springInitializrRequest);
        if (!springInitializrService.dependencyPresent(springInitializrRequest.getDependencies())) {
            return new ResponseEntity<>(new SpringInitializrResponse(HttpStatus.BAD_REQUEST, Constants.DEPENDENCY_NOT_PRESENT), HttpStatus.BAD_REQUEST);
        }
        return springInitializrService.downloadTemplate(httpServletResponse, springInitializrRequest);
    }

    @GetMapping("dependencies")
    public ResponseEntity<?> getDependencies() {
        List<String> dependencies = springInitializrService.getDependencies();
        Map<String, List<String>> dependenciesMap = Map.of(Constants.DEPENDENCIES, dependencies);
        log.info("Dependencies: {}", dependenciesMap);
        return new ResponseEntity<>(dependenciesMap, HttpStatus.OK);
    }

}
