package ru.altarix.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocController {

    @RequestMapping("/")
    public String test() {
        return "Hello world";
    }
}
