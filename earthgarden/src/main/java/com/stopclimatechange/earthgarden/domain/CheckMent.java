package com.stopclimatechange.earthgarden.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckMent {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String ment;
}

