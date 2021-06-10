package com.wilgur513.springsecurityjwt;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/admin")
    public String test() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

}
