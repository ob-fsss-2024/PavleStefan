package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    private  final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/hello/{id}")
    public String helloWorld(@RequestParam String name, @PathVariable String id){
        return testService.helloWorld(name,id);
    }



@PostMapping("/hello")
public String helloWorldPost(@RequestBody User user){
        return "Hello " + user.name() + " "+ user.age();
    }
}

record User(String name, int age) {}
