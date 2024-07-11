package com.example.demo.nationalize.client;

import com.example.demo.nationalize.client.dto.NatResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface NatClient {
    @GetExchange("/?name={name}")
    NatResponse getNationality(@PathVariable String name);
}
