package com.example.demo.nationalize.client.dto;

import java.util.List;

public record NatResponse(Integer count,String name, List<NatCountry> country) {

}
