package day.dayBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
