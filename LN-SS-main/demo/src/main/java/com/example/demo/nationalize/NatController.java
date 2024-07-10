package com.example.demo.nationalize;

import com.example.demo.nationalize.client.NatClient;
import com.example.demo.nationalize.client.dto.NatCountry;
import com.example.demo.nationalize.client.dto.NatResponse;
import org.springframework.web.bind.annotation.*;
import com.example.demo.nationalize.client.dto.CountryUtils;

import java.util.List;

import static org.aspectj.apache.bcel.generic.InstructionConstants.bla;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("home")
public class NatController {
    private final NatClient natClient;

    public NatController(final NatClient natClient) {
        this.natClient = natClient;
    }

    @GetMapping("/name")
    public String getNationality(@RequestParam final String name) {
        NatResponse object = natClient.getNationality(name);
        String fullName = object.name();
        List<NatCountry> countries = object.country();
        if (countries != null && !countries.isEmpty()) {
            NatCountry selectedCountry = countries.get(0);
            String fullCountryName = CountryUtils.getCountryFullName(selectedCountry.country_id());
            double prob = Math.round(selectedCountry.probability()*100.0f);
            String countryProb = String.valueOf(prob);
            return  fullName + " is from " + fullCountryName + " with " + countryProb + "%" ;
        } else {
            return "No countries available";
        }

    }
}
