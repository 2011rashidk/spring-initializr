package org.happiest.minds.springinitializr.controller;

import org.happiest.minds.springinitializr.request.SpringInitializrRequest;
import org.happiest.minds.springinitializr.service.SpringInitializrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/spring/initializr")
public class SpringInitializrController {

    @Autowired
    SpringInitializrService springInitializrService;

    @GetMapping("zip")
    public void downloadTemplate(@RequestBody SpringInitializrRequest springInitializrRequest) {

    }

}
