package com.example.demo.nationalize;

import com.example.demo.nationalize.client.NatClient;
import com.example.demo.nationalize.client.dto.NatResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("home")
public class NatController {
    private final NatClient natClient;

    public NatController(final NatClient natClient) {
        this.natClient = natClient;
    }

    @GetMapping("/name")
    public NatResponse getNationality(@RequestParam final String name) {
        return natClient.getNationality(name);
    }
}
