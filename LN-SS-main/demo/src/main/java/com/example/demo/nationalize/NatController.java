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
import com.example.demo.wikipedia.WikipediaService;
import com.example.demo.wikipedia.dto.WikipediaData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final WikipediaService wikipediaService;



    public NatController(final NatClient natClient, AzureOpenAiChatModel chatModel, MeterRegistry meterRegistry, WikipediaService wikipediaService) {
        this.natClient = natClient;
        this.chatModel = chatModel;
        this.chatGPTCall = meterRegistry.counter("chat_gpt_call");
        this.chatGPTTime = meterRegistry.timer("chat_gpt_time");
        this.wikipediaService = wikipediaService;
    }

    @GetMapping("/name")
    public Map<String, String> getNationality(@RequestParam final String name) {
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
            List<WikipediaData> wikipediaDataList = wikipediaService.findByTitleRepository(fullCountryName);
            String wikipedia;
            logger.info("Wiki start");
            if (!wikipediaDataList.isEmpty()) {
                wikipedia = wikipediaDataList.get(0).auxiliary_text();
            } else {
                wikipedia = "No data";
            }
            logger.info("Wiki done");
            logger.info("Chat start");
            String chat =  chatModel.call("Give me 5 interesting facts about " + fullCountryName + ". Please just list them without any text before");
            chatGPTCall.increment();
            String chat2 =  chatModel.call("Give me 3 jokes most used in " + fullCountryName + ". Please just list them without any text before" );
            chatGPTCall.increment();
            String chat3 =  chatModel.call("Tell me 10 basic and simple phrases in the language used in " + fullCountryName + ". Please just list them without any text before");
            logger.info("Chat done");

            String formattedChat = chat.replace("\n", "<br>");
            String formattedChat2 = chat2.replace("\n", "<br>");
            String formattedChat3 = chat3.replace("\n", "<br>");

            chatGPTCall.increment();
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("headline", headline);
            jsonObject.put("prompt1", formattedChat);
            jsonObject.put("prompt2", formattedChat2);
            jsonObject.put("prompt3", formattedChat3);
            jsonObject.put("wiki", wikipedia);
            //
            // Return the map, which will be automatically converted to JSON by Spring Boot
            return jsonObject;
        } else {
            Map<String, String> jsonObject = new HashMap<>();
            String notFound ="No countries available";
            jsonObject.put("headline", notFound);
            return jsonObject;
        }

    }
}
