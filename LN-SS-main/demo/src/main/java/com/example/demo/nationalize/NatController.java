package com.example.demo.nationalize;

import com.example.demo.nationalize.client.NatClient;
import com.example.demo.nationalize.client.dto.NatCountry;
import com.example.demo.nationalize.client.dto.NatResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.web.bind.annotation.*;
import com.example.demo.nationalize.client.dto.CountryUtils;

import java.util.List;

import static org.aspectj.apache.bcel.generic.InstructionConstants.bla;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("home")
public class NatController {
    private final NatClient natClient;
    private final AzureOpenAiChatModel chatModel;
    private final Logger logger = LoggerFactory.getLogger(NatController.class.getName());
    private final Counter chatGPTCall;
    private final Timer chatGPTTime;



    public NatController(final NatClient natClient, AzureOpenAiChatModel chatModel, MeterRegistry meterRegistry) {
        this.natClient = natClient;
        this.chatModel = chatModel;
        this.chatGPTCall = meterRegistry.counter("chat_gpt_call");
        this.chatGPTTime = meterRegistry.timer("chat_gpt_time");
    }

    @GetMapping("/name")
    public String getNationality(@RequestParam final String name) {
        NatResponse object = natClient.getNationality(name);
        String fullName = object.name();
        List<NatCountry> countries = object.country();

        if (countries != null && !countries.isEmpty()) {
            NatCountry selectedCountry = countries.get(0);
            String fullCountryName = CountryUtils.getCountryFullName(selectedCountry.country_id());
            logger.info("Fetching node " + fullCountryName);
            double prob = Math.round(selectedCountry.probability()*100.0f);
            String countryProb = String.valueOf(prob);
            String headline = fullName + " is from " + fullCountryName + " with " + countryProb + "%" ;

            String chat =  chatModel.call("Give me 5 interesting facts about " + fullCountryName);
            chatGPTCall.increment();
            String chat2 =  chatModel.call("Give me 3 jokes most used in " + fullCountryName);
            chatGPTCall.increment();
            String chat3 =  chatModel.call("Tell me 10 basic and simple phrases in the language used in " + fullCountryName);

            chatGPTCall.increment();
            return headline + "\n" + chat + "\n" + chat2 + "\n" + chat3;
        } else {
            return "No countries available";
        }

    }
}
