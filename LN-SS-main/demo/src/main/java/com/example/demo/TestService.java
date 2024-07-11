package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final String something;

    public TestService(@Value("${something.something}") String something) {
        this.something = something;
    }

    public String helloWorld(String name, String id){
        return "Hello " + name + id + " " + something;


    }
}
