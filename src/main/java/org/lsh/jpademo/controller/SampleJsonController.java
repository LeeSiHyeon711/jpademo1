package org.lsh.jpademo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SampleJsonController {
    @GetMapping("/hello10")
    public String hello() {
        return "hello ";
    }
    @GetMapping("/helloArr10")
    public String[] helloArr() {
        return new String[]{"hello", "world"};
    }
}
