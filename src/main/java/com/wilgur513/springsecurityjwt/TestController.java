package com.wilgur513.springsecurityjwt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/asdf")
    public String test() {
        return "asdf";
    }
}
